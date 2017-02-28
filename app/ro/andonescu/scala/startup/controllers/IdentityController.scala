package ro.andonescu.scala.startup.controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import ro.andonescu.scala.startup.controllers.jsons.IdentityWithLogin
import ro.andonescu.scala.startup.services.IdentityService

import scala.concurrent.{ExecutionContext, Future}

class IdentityController @Inject() (service: IdentityService, implicit val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  def view = Action {
    Ok(views.html.userForm(userForm))
  }

  val userForm: Form[IdentityWithLogin] = Form(
    mapping(
      "First name" -> nonEmptyText,
      "Last name" -> nonEmptyText,
      "Email" -> nonEmptyText,
      "Username" -> nonEmptyText,
      "Password" -> nonEmptyText
    )(IdentityWithLogin.apply)(IdentityWithLogin.unapply)
  )

  def addAnIdentity = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      errors => {
        Future.successful(Ok(views.html.userForm(errors)))
      },
      form => {

        service.save(form).map {
          case Right(_) =>
            Redirect(routes.ApplicationController.homepage())
          case Left(err) =>
            ???
        }
      }
    )
  }

}
