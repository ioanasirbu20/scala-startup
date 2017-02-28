package ro.andonescu.scala.startup.validations

import com.google.inject.Inject
import ro.andonescu.scala.startup.controllers.jsons.LoginForm
import ro.andonescu.scala.startup.models.LoginRepository
import ro.andonescu.scala.startup.validations.errors.ErrorMessage

import scala.concurrent.ExecutionContext

class LoginValidation @Inject() (repoLogin: LoginRepository)(implicit ec: ExecutionContext) {

  def usernameAndPassword(login: LoginForm) = {
    repoLogin.byUsername(login.username).map {
      case Some(user) if login.password == user.password => Seq.empty
      case None => Seq(ErrorMessage("credentials", "The username or password is wrong."))
    }
  }

}
