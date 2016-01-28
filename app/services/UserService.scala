package myapp.services

import cats.data.Xor
import cats.syntax.option._
import cats.syntax.xor._
import javax.inject.Inject;
import myapp.models._
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import slick.driver.H2Driver.api._
import slick.driver.JdbcProfile

class UserService @Inject()(dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  def get: Future[Throwable Xor User] = {
    val q = users.filter(_.email === "foo@example.org")
    val maybeUser = dbConfig.db.run(q.result.headOption)
    maybeUser.map(_ toRightXor(new Exception("User not found"))).recover {
      case t => t.left
    }
  }
}
