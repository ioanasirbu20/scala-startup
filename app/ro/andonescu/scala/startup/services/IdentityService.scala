package ro.andonescu.scala.startup.services

import com.google.inject.Inject
import ro.andonescu.scala.startup.controllers.jsons.IdentityWithLogin
import ro.andonescu.scala.startup.models.entity.{Identity, IdentityLogin, Login}
import ro.andonescu.scala.startup.models.{IdentityLoginRepository, IdentityRepository, LoginRepository}
import ro.andonescu.scala.startup.validations.SignupValidation
import ro.andonescu.scala.startup.validations.errors.ErrorMessage

import scala.concurrent.{ExecutionContext, Future}

trait IdentityService {
  def findAll: Future[Seq[Identity]]

  def save(i: IdentityWithLogin): Future[Either[Seq[ErrorMessage], Long]]
}

class IdentityServiceImpl @Inject() (
    repo: IdentityRepository,
    repoLogin: LoginRepository,
    repoIdentityLogin: IdentityLoginRepository,
    loginService: LoginServiceImpl,
    validator: SignupValidation
)(implicit ec: ExecutionContext) extends IdentityService {
  override def findAll: Future[Seq[Identity]] = repo.findAll

  override def save(i: IdentityWithLogin): Future[Either[Seq[ErrorMessage], Long]] = {
    validator.signupValidation(i).flatMap {
      case Nil => {
        val encryptedPassword = loginService.encryptPassword(i.password)
        val identityId = repo.save(Identity(0, i.firstName, i.lastName, i.email))
        val loginId = repoLogin.save(Login(0, i.username, encryptedPassword))

        for {
          iID <- identityId
          lID <- loginId
          _ <- repoIdentityLogin.save(IdentityLogin(iID, lID))
        } yield iID
      }.map(v => Right(v))
      case errors => {
        println(errors)
        Future.successful(Left(errors))
      }
    }

  }
}