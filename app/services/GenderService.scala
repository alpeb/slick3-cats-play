package myapp.services

import cats.data.Xor
import cats.syntax.xor._
import java.io.IOException
import javax.inject.Inject
import play.api.libs.ws._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GenderService @Inject()(ws: WSClient) {

  val Key = "O2pamJehRemsh2AZLeR1RKXY7o78p11FBKijsnsmj9bemjMcN4"

  def get(name: String): Future[Throwable Xor String] = {
    val url = s"https://montanaflynn-gender-guesser.p.mashape.com/"
    val request = ws.url(url)
      .withHeaders("X-Mashape-Key" -> Key)
      .withHeaders("Accept" -> "application/json")
      .withQueryString("name" -> name)
      .withRequestTimeout(3000)

    val res = request.get()
    res.map(res => (res.json \ "description").as[String].right).recover {
      case t => t.left
    }
  }
}
