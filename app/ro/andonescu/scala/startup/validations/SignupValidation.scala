package ro.andonescu.scala.startup.validations

import com.google.inject.Inject
import ro.andonescu.scala.startup.controllers.jsons.IdentityWithLogin
import ro.andonescu.scala.startup.models.{IdentityRepository, LoginRepository}
import ro.andonescu.scala.startup.validations.errors.ErrorMessage

import scala.concurrent.{ExecutionContext, Future}

class SignupValidation @Inject() (loginRepo: LoginRepository, identityRepo: IdentityRepository)(implicit ec: ExecutionContext) {
  private def isUsernameUnique(username: String): Future[Seq[ErrorMessage]] = {
    loginRepo.byUsername(username) map {
      case Some(_) => Seq(ErrorMessage("username", "This username is already used"))
      case None    => Seq.empty
    }
  }

  private def isPasswordLongEnough(password: String): Future[Seq[ErrorMessage]] = {
    password.length match {
      case n if n < 8 => Future.successful(Seq(ErrorMessage("password", "The password is too short.")))
      case n          => Future.successful(Seq.empty)
    }
  }

  private def isEmailAddressValid(email: String): Future[Seq[ErrorMessage]] = {
    email.contains("@") match {
      case true  => Future.successful(Seq.empty)
      case false => Future.successful(Seq(ErrorMessage("email", "The email address is invalid.")))
    }
  }

  private def isEmailAddressUnique(email: String): Future[Seq[ErrorMessage]] = {
    identityRepo.countByEmail(email) map {
      case 0 => Seq.empty
      case _ => Seq(ErrorMessage("email", "There is already an account with this email."))
    }
  }

  def signupValidation(identityWithLogin: IdentityWithLogin) = {
    for {
      username <- isUsernameUnique(identityWithLogin.username)
      password <- isPasswordLongEnough(identityWithLogin.password)
      emailValid <- isEmailAddressValid(identityWithLogin.email)
      emailUnique <- isEmailAddressUnique(identityWithLogin.email)
    } yield username ++ password ++ emailUnique ++ emailValid
  }
}
