package ro.andonescu.scala.startup.validations

import java.util.Calendar

import com.google.inject.{Inject, Singleton}
import ro.andonescu.scala.startup.controllers.jsons.FilmForm
import ro.andonescu.scala.startup.models.entity.Film
import ro.andonescu.scala.startup.models.{ActorRepository, CategoryRepository, FilmRepository, LanguageRepository}
import ro.andonescu.scala.startup.validations.errors.ErrorMessage

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FilmValidation @Inject() (repo: FilmRepository, langRepo: LanguageRepository, actorRepo: ActorRepository, categoryRepo: CategoryRepository)(implicit ec: ExecutionContext) {

  def isTitleValid(f: FilmForm): Future[Seq[ErrorMessage]] = {
    repo.byTitle(f.title).map {
      case Some(_) => Seq(ErrorMessage("title", "Film with that title already saved."))
      case None    => Seq.empty
    }
  }

  def isLanguageIdValid(f: FilmForm): Future[Seq[ErrorMessage]] = {
    langRepo.idCheck(f.languageId).map {
      case Some(_) => Seq.empty
      case None    => Seq(ErrorMessage("languageId", "The languageId does not exist"))
    }
  }

  def isRatingValid(f: FilmForm): Seq[ErrorMessage] = {
    val ratingList = List("NC-17", "PG-13", "R", "G", "PG")
    def ratingCheck(ratingList: List[String]): Seq[ErrorMessage] = ratingList match {
      case Nil                                => Seq(ErrorMessage("rating", "Rating invalid"))
      case head :: tail if (head == f.rating) => Seq.empty
      case _                                  => ratingCheck(ratingList.tail)
    }

    ratingCheck(ratingList)
  }

  def isReleaseYearValid(f: FilmForm): Seq[ErrorMessage] = f.releaseYear match {
    case a if a <= Calendar.getInstance().get(Calendar.YEAR) && a > 1920 => Seq.empty
    case _ => Seq(ErrorMessage("releaseYear", "ReleaseYear not valid"))
  }

  def titleValidUpdate(f: FilmForm, id: Long): Future[Seq[ErrorMessage]] = {
    repo.existsByTitleAndNotId(f.title, id).map {
      case true  => Seq(ErrorMessage("title", "title already exists"))
      case false => Seq.empty
    }
  }

  def idValid(id: Long): Future[Seq[ErrorMessage]] = {
    repo.existsById(id).map {
      case true  => Seq.empty
      case false => Seq(ErrorMessage("id", "id does not exist"))
    }
  }

  def existsActor(f: FilmForm): Future[Seq[ErrorMessage]] = {
    actorRepo.existsId(f.actors).map(ids => {
      if (ids.length == f.actors.length) Seq.empty
      else Seq(ErrorMessage("actors", "actor id does not exist"))
    })
  }

  def existsCategory(f: FilmForm): Future[Seq[ErrorMessage]] = {
    categoryRepo.existIds(f.category).map(ids => {
      if (ids.length == f.category.length) Seq.empty
      else Seq(ErrorMessage("category", "category id does not exist"))
    })
  }

  def saveValidation(f: FilmForm) = {
    for {
      titleValid <- isTitleValid(f)
      languageIdValid <- isLanguageIdValid(f)
      existsActor <- existsActor(f)
      existsCategory <- existsCategory(f)
    } yield titleValid ++ languageIdValid ++ isReleaseYearValid(f) ++ isRatingValid(f) ++ existsActor ++ existsCategory
  }

  def updateValidation(f: FilmForm, id: Long) = {
    for {
      languageIdValid <- isLanguageIdValid(f)
      titleValid <- titleValidUpdate(f, id)
    } yield languageIdValid ++ isReleaseYearValid(f) ++ isRatingValid(f) ++ titleValid
  }

  def deleteValid(f: Film) = {
    idValid(f.id)
  }
}
