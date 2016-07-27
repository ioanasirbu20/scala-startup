package ro.andonescu.scala.startup.validations

import java.util.Calendar

import com.google.inject.{Inject, Singleton}
import ro.andonescu.scala.startup.controllers.jsons.FilmForm
import ro.andonescu.scala.startup.models.entity.Film
import ro.andonescu.scala.startup.models.{FilmRepository, LanguageRepository}

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/27/2016.
 */
@Singleton
class FilmValidation @Inject() (repo: FilmRepository, langRepo: LanguageRepository)(implicit ec: ExecutionContext) {

  def isTitleValid(f: FilmForm): Future[Boolean] = {
    repo.byTitle(f.title).map {
      case Some(_) => false
      case None    => true
    }
  }

  def isLanguageIdValid(f: FilmForm): Future[Boolean] = {
    langRepo.idCheck(f.languageId).map {
      case Some(_) => true
      case None    => false
    }
  }

  def isRatingValid(f: FilmForm): Boolean = {
    val ratingList = List("NC-17", "PG-13", "R", "G", "PG")
    def ratingCheck(ratingList: List[String]): Boolean = ratingList match {
      case Nil                                => false
      case head :: tail if (head == f.rating) => true
      case _                                  => ratingCheck(ratingList.tail)
    }

    ratingCheck(ratingList)
  }

  def isReleaseYearValid(f: FilmForm): Boolean = f.releaseYear <= Calendar.getInstance().get(Calendar.YEAR) && f.releaseYear > 1920

  def saveValidation(f: FilmForm) = {
    for {
      titleValid <- isTitleValid(f)
      languageIdValid <- isLanguageIdValid(f)
    } yield titleValid && languageIdValid && isReleaseYearValid(f) && isRatingValid(f)
  }

  def updateValidation(f: FilmForm) = {
    for {
      languageIdValid <- isLanguageIdValid(f)
    } yield languageIdValid && isReleaseYearValid(f) && isRatingValid(f)
  }
}
