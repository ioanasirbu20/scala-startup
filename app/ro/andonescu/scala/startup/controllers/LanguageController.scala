package ro.andonescu.scala.startup.controllers

import play.api.mvc.{Action, Controller}
import ro.andonescu.scala.startup.models.LanguageRepository
import com.google.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import ro.andonescu.scala.startup.controllers.views.LanguagesView
import ro.andonescu.scala.startup.models.entity.Language

import scala.concurrent.ExecutionContext

/**
 * Created by V3790155 on 7/21/2016.
 */
class LanguageController @Inject() (repo: LanguageRepository, implicit val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  def getLanguages = Action.async {
    repo.findAll().map { languages =>
      Ok(Json.toJson(LanguagesView(languages)))
    }
  }
}
