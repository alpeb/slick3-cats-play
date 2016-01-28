package myapp

import slick.driver.H2Driver.api._

package object models {

  case class User(id: Int, email: String, name: String)

  class UserRow(tag: Tag) extends Table[User](tag, "USER") {
    def id = column[Int]("ID", O.PrimaryKey)
    def email = column[String]("EMAIL")
    def name = column[String]("NAME")
    def * = (id, email, name) <> ((User.apply _).tupled, User.unapply)
  }

  val users = TableQuery[UserRow]
}
