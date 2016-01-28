package myapp.controllers

import cats.data.XorT
import cats.std.future._
import javax.inject.Inject;
import myapp.services._
import play.api._
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global

class Application @Inject() (userService: UserService, genderService: GenderService) extends Controller {

  def index = Action.async {
    val res = for {
      user              <- XorT(userService.get)
      genderDescription <- XorT(genderService.get(user.name))
    } yield {
      Ok(views.html.index(s"Hello ${user.name}. $genderDescription"))
    }

    res.value.map(_.fold(err => InternalServerError(err.getMessage), identity))
  }
}
