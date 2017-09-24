import java.sql.DriverManager

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import slick.basic.DatabasePublisher
import slick.jdbc.GetResult
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object Main extends App {

  implicit val system = ActorSystem("slick-app")
  implicit val materializer = ActorMaterializer()

  println("Running.....!")

  val dbUrl= "jdbc:jtds:sqlserver://localhost:1433/northwind;user=yarshad;password=welcome"
  val con = DriverManager.getConnection( dbUrl )
  val db = Database.forURL(dbUrl)


  case class Shipper(shipperID: Int)

  implicit val ShipperResult = GetResult(r => Shipper(r.nextInt))

  def getShippers() : DatabasePublisher[Shipper] = {

   val query = sql"""Select ShipperID as 'ShipperID'  from Shippers""".as[Shipper]

    val stream = db.stream(query.withStatementParameters(fetchSize = 100).transactionally) //.withStatementParameters(statementInit = enableJdbcStreaming))
     stream
  }

  val source = Source.fromPublisher(getShippers())
  val sink = Sink.foreach[Shipper](f => println(f))

  val complete = source.runWith(sink)

  Await.result(complete, Duration.Inf)

}