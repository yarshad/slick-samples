import java.sql.DriverManager

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.stream.scaladsl.Source
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Infinite extends App {

  implicit val system = ActorSystem("Publisher")
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val dbUrl= "jdbc:jtds:sqlserver://localhost:1433/northwind;user=yarshad;password=welcome"
  val con = DriverManager.getConnection( dbUrl )
  val db = Database.forURL(dbUrl)

  val queryLimit = 10

  try {

    println("Running.....")
    val newRecStream = Source.unfoldAsync(0) { n =>
      val q = TableQuery[Shippers].filter(row => row.id > n).take(queryLimit)
      db.run(q.result).map { recs =>
//        Some(recs.last.id, recs)
        val lastId = if(recs.isEmpty) n else recs.last.id
        Some(lastId, recs)
      }
    }
      .throttle(1, 1.second, 1, ThrottleMode.shaping)
      .flatMapConcat { recs =>
        Source.fromIterator(() => recs.iterator)
      }
      .runForeach { rec =>
        println(s"${rec.id}, ${rec.company}")
      }

    Await.ready(newRecStream, Duration.Inf)
  }
  catch
    {
      case ex: Throwable => println(ex)
    }
  finally {
    println("Db closed...")
//    system.shutdown
    db.close
  }


}