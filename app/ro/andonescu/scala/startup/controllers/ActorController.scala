package ro.andonescu.scala.startup.controllers

import com.google.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import ro.andonescu.scala.startup.controllers.jsons.{ActorForm, ActorsView}
import ro.andonescu.scala.startup.services.ActorsService

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/22/2016.
 */
class ActorController @Inject() (service: ActorsService, implicit val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  def getActors = Action.async {
    service.findAll().map { actors =>
      Ok(Json.toJson(ActorsView(actors)))
    }
  }

  def addActor = Action.async(parse.json) { request =>

    request.body.validate[ActorForm].fold(
      errors => {
        Future.successful(BadRequest("Expecting json data"))
      },
      form => {
        //
        service.save(form).map {
          case Right(id)      => Ok(s"Saved $id")
          case Left(errorMsg) => BadRequest(errorMsg)
        }
      }
    )
  }

  def removeActor(id: Long) = Action.async {
    service.delete(id).map {
      case 0 => BadRequest("")
      case _ => Ok("deleted")
    }
  }
}
