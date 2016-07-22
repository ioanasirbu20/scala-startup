package ro.andonescu.scala.startup.controllers

import com.google.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import ro.andonescu.scala.startup.controllers.jsons.ActorView
import ro.andonescu.scala.startup.controllers.jsons.ActorView.actorViewFormat
import ro.andonescu.scala.startup.models.ActorRepository
import ro.andonescu.scala.startup.models.entity.Actor

import scala.concurrent.ExecutionContext

/**
 * Created by V3790155 on 7/22/2016.
 */
class ActorController @Inject() (repo: ActorRepository, implicit val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  def getActors = Action.async {
    repo.findAll().map { actors =>
      Ok(Json.toJson(ActorView(actors)))
    }
  }

}
