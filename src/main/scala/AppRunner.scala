import java.sql.DriverManager

import slick.lifted.{TableQuery}
import slick.jdbc.SQLServerProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object AppRunner extends App{

  val shippers = TableQuery[Shippers]

  val dbUrl= "jdbc:jtds:sqlserver://localhost:1433/northwind;user=yarshad;password=welcome"
  val con = DriverManager.getConnection( dbUrl )
  val db = Database.forURL(dbUrl)

  val x =   db.run(shippers.result).map(_.foreach(f => println(f)))
  Await.result(x, Duration.Inf)

}
