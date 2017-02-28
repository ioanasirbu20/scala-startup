package ro.andonescu.scala.startup.services

import com.google.inject.Inject
import org.mindrot.jbcrypt.BCrypt
import ro.andonescu.scala.startup.controllers.jsons.LoginForm
import ro.andonescu.scala.startup.models.LoginRepository
import ro.andonescu.scala.startup.models.entity.Login
import ro.andonescu.scala.startup.validations.LoginValidation
import ro.andonescu.scala.startup.validations.errors.ErrorMessage

import scala.concurrent.{ExecutionContext, Future}

trait LoginService {
  def save(l: Login): Future[Long]

  def login(user: LoginForm): Future[Either[Seq[ErrorMessage], Long]]
}

class LoginServiceImpl @Inject() (repo: LoginRepository, validator: LoginValidation)(implicit ec: ExecutionContext) extends LoginService {
  override def save(l: Login): Future[Long] = repo.save(l)

  def encryptPassword(pwd: String) = BCrypt.hashpw(pwd, BCrypt.gensalt())

  def login(user: LoginForm): Future[Either[Seq[ErrorMessage], Long]] = validator.usernameAndPassword(user).flatMap {
    case Nil => {
      Future.successful(Right(1))
    }
    case errors => {
      Future.successful(Left(errors))
    }
  }
}
