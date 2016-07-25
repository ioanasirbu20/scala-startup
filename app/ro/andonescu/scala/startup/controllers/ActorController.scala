package ro.andonescu.scala.startup.controllers

import com.google.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import ro.andonescu.scala.startup.controllers.jsons.{ActorForm, ActorsView}
import ro.andonescu.scala.startup.models.ActorRepository

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/22/2016.
 */
class ActorController @Inject() (repo: ActorRepository, implicit val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  def getActors = Action.async {
    repo.findAll().map { actors =>
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
        repo.save(form).map {
          id => Ok("Saved")
        }
      }
    )
  }

  def removeActor(id: Long) = Action.async {
    repo.delete(id).map { request =>
      Ok("deleted")
    }
  }

}
