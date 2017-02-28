package ro.andonescu.scala.startup.controllers

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import ro.andonescu.scala.startup.controllers.jsons.LoginForm
import ro.andonescu.scala.startup.services.LoginService

import scala.concurrent.{ExecutionContext, Future}

class LoginController @Inject() (service: LoginService, identityController: IdentityController, implicit val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {
  def view = Action {
    Ok(views.html.loginForm(userForm))
  }

  val userForm: Form[LoginForm] = Form(
    mapping(
      "Username" -> nonEmptyText,
      "Password" -> nonEmptyText
    )(LoginForm.apply)(LoginForm.unapply)
  )

  def login = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      errors => {
        Future.successful(Ok(views.html.loginForm(errors)))
      },
      form => {
        service.login(form).map { _ =>
          Redirect(routes.ApplicationController.homepage())
        }
      }
    )
  }

}
