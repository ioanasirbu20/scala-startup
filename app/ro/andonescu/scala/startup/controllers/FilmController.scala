package ro.andonescu.scala.startup.controllers

import akka.io.Tcp.Message
import com.google.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import ro.andonescu.scala.startup.controllers.jsons.FilmsView
import ro.andonescu.scala.startup.models.FilmRepository

import scala.concurrent.ExecutionContext

/**
 * Created by V3790155 on 7/25/2016.
 */
class FilmController @Inject() (repo: FilmRepository, implicit val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  def getFilms = Action.async {
    repo.findAll().map { films =>
      Ok(Json.toJson(FilmsView(films)))
    }
  }
}
