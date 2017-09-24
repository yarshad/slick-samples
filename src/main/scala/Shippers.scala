import slick.jdbc.SQLServerProfile.api._
import slick.lifted.Tag



case class Shipper(id: Int, company: String, phone: String)

class Shippers(tag: Tag) extends Table[Shipper](tag, "Shippers") {
  def id = column[Int]("ShipperID", O.PrimaryKey) // This is the primary key column
  def company = column[String]("CompanyName")
  def phone = column[String]("Phone")
  // Every table needs a * projection with the same type as the table's type parameter
//  def * = (id, company,phone)
  def * = (id, company, phone) <> (Shipper.tupled, Shipper.unapply)

}
