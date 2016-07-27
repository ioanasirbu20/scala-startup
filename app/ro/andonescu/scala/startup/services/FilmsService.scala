package ro.andonescu.scala.startup.services

import java.util.Calendar

import com.google.inject.{Inject, Singleton}
import ro.andonescu.scala.startup.controllers.jsons.FilmForm
import ro.andonescu.scala.startup.models.{FilmRepository, LanguageRepository}
import ro.andonescu.scala.startup.models.entity.Film
import ro.andonescu.scala.startup.validations.FilmValidation

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/26/2016.
 */
trait FilmsService {
  def findAll(): Future[Seq[Film]]

  def save(f: FilmForm): Future[Either[String, Long]]

  def delete(id: Int): Future[Int]

  def update(id: Long, f: FilmForm): Future[Either[String, Long]]
}

@Singleton
class FilmsServiceImpl @Inject() (repo: FilmRepository, validation: FilmValidation)(implicit ec: ExecutionContext) extends FilmsService {
  override def findAll(): Future[Seq[Film]] = {
    repo.findAll()
  }

  override def save(f: FilmForm): Future[Either[String, Long]] = {

    validation.saveValidation(f).flatMap { isValid =>
      if (isValid)
        repo.save(Film(0, f.title, f.description, f.releaseYear, f.languageId, f.originalLanguageId, f.rentalDuration, f.rentalRate, f.length, f.replacementCost, f.rating)).map((v => Right(v)))
      else
        Future.successful(Left("Failed"))
    }
  }

  override def delete(id: Int): Future[Int] = {
    repo.delete(id)
  }

  override def update(id: Long, f: FilmForm): Future[Either[String, Long]] = {
    validation.updateValidation(f).flatMap { isValid =>
      if (isValid)
        repo.update(Film(id, f.title, f.description, f.releaseYear, f.languageId, f.originalLanguageId, f.rentalDuration, f.rentalRate, f.length, f.replacementCost, f.rating)).map((v => Right(v)))
      else
        Future.successful(Left("failed"))
    }
  }
}
