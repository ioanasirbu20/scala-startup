package ro.andonescu.scala.startup.controllers

import akka.io.Tcp.Message
import com.google.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import ro.andonescu.scala.startup.controllers.jsons.{FilmForm, FilmsView}
import ro.andonescu.scala.startup.models.FilmRepository
import ro.andonescu.scala.startup.services.FilmsService

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/25/2016.
 */
class FilmController @Inject() (service: FilmsService, implicit val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  def getFilms = Action.async {
    service.findAll().map { films =>
      Ok(Json.toJson(FilmsView(films)))
    }
  }

  def addFilm = Action.async(parse.json) { request =>
    request.body.validate[FilmForm].fold(
      errors => {
        Future.successful(BadRequest("Expecting json data"))
      },
      form => {
        service.save(form).map {
          case Right(id)      => Ok("saved")
          case Left(errorMsg) => BadRequest(errorMsg)
        }
      }
    )
  }
}
