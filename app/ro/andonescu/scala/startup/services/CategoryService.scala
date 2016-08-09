package ro.andonescu.scala.startup.services

import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime
import ro.andonescu.scala.startup.controllers.jsons.CategoryForm
import ro.andonescu.scala.startup.models.CategoryRepository
import ro.andonescu.scala.startup.models.entity.Category
import ro.andonescu.scala.startup.validations.CategoryValidation
import ro.andonescu.scala.startup.validations.errors.ErrorMessage

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 8/9/2016.
 */
trait CategoryService {
  def save(category: CategoryForm): Future[Either[Seq[ErrorMessage], Long]]

  def findAll(): Future[Seq[Category]]
}

@Singleton
class CategoryServiceImpl @Inject() (repo: CategoryRepository, validation: CategoryValidation)(implicit ec: ExecutionContext) extends CategoryService {

  override def save(c: CategoryForm): Future[Either[Seq[ErrorMessage], Long]] = {
    validation.isNameValid(c).flatMap {
      case Nil => {
        repo.save(Category(0, c.name, DateTime.now))
      }.map((v => Right(v)))
      case errors =>
        Future.successful(Left(errors))
    }
  }

  override def findAll(): Future[Seq[Category]] = {
    repo.findAll()
  }
}
