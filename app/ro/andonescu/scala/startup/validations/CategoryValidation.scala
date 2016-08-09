package ro.andonescu.scala.startup.validations

import com.google.inject.Inject
import ro.andonescu.scala.startup.controllers.jsons.CategoryForm
import ro.andonescu.scala.startup.models.CategoryRepository
import ro.andonescu.scala.startup.validations.errors.ErrorMessage

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 8/9/2016.
 */
class CategoryValidation @Inject() (repo: CategoryRepository)(implicit ec: ExecutionContext) {
  def isNameValid(c: CategoryForm): Future[Seq[ErrorMessage]] = {
    repo.byName(c.name).map {
      case Some(_) => Seq(ErrorMessage("name", "name already exists"))
      case None    => Seq.empty
    }
  }

}
