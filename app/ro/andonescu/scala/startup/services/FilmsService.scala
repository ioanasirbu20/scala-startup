package ro.andonescu.scala.startup.services

import com.google.inject.{Inject, Singleton}
import ro.andonescu.scala.startup.controllers.jsons.FilmForm
import ro.andonescu.scala.startup.models.{FilmRepository, LanguageRepository}
import ro.andonescu.scala.startup.models.entity.Film

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/26/2016.
 */
trait FilmsService {
  def findAll(): Future[Seq[Film]]

  def save(f: FilmForm): Future[Either[String, Long]]
}

@Singleton
class FilmsServiceImpl @Inject() (repo: FilmRepository, langRepo: LanguageRepository)(implicit ec: ExecutionContext) extends FilmsService {
  override def findAll(): Future[Seq[Film]] = {
    repo.findAll()
  }

  def isFilmValid(f: FilmForm): Future[Boolean] = {

    def isLanguageIdValid(): Future[Boolean] = {
      langRepo.idCheck(f.languageId).map {
        case Some(_) => true
        case None    => false
      }
    }

    def isRatingValid(): Boolean = {
      val ratingList = List("NC-17", "PG-13", "R", "G", "PG")
      def ratingCheck(ratingList: List[String]): Boolean = ratingList match {
        case Nil                                => false
        case head :: tail if (head == f.rating) => true
        case _                                  => ratingCheck(ratingList.tail)
      }

      ratingCheck(ratingList)
    }

    def isReleaseYearValid(): Boolean = f.releaseYear <= 2016 && f.releaseYear > 1920

    isLanguageIdValid().map { isLanguageValid =>
      isLanguageValid && isRatingValid() && isReleaseYearValid()
    }
  }
  override def save(f: FilmForm): Future[Either[String, Long]] = {

    isFilmValid(f).flatMap { isValid =>
      if (isValid)
        repo.save(Film(0, f.title, f.description, f.releaseYear, f.languageId, f.originalLanguageId, f.rentalDuration, f.rentalRate, f.length, f.replacementCost, f.rating)).map((v => Right(v)))
      else
        Future.successful(Left("Failed"))
    }
  }
}
