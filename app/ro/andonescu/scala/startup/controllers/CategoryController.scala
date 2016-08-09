package ro.andonescu.scala.startup.controllers

import com.google.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import ro.andonescu.scala.startup.controllers.jsons.{CategoryForm, CategoryView}
import ro.andonescu.scala.startup.models.entity.Category
import ro.andonescu.scala.startup.services.CategoryService
import ro.andonescu.scala.startup.validations.errors.ErrorMessages

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 8/9/2016.
 */
class CategoryController @Inject() (repo: CategoryService, implicit val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {
  def addCategory = Action.async(parse.json) { request =>
    request.body.validate[CategoryForm].fold(
      errors => {
        Future.successful(BadRequest("Expecting Json data"))
      },
      form => {
        repo.save(form).map {
          case Right(id)      => Ok("saved")
          case Left(errorMsg) => BadRequest(Json.toJson(ErrorMessages("POST /V1/categories", errorMsg)))
        }
      }
    )

  }

  def getCategories = Action.async {
    repo.findAll().map { category =>
      Ok(Json.toJson(CategoryView(category)))
    }
  }

}
