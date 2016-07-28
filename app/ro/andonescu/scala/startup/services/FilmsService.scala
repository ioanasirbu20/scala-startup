package ro.andonescu.scala.startup.services

import java.util.Calendar

import com.google.inject.{Inject, Singleton}
import ro.andonescu.scala.startup.controllers.jsons.FilmForm
import ro.andonescu.scala.startup.models.{FilmRepository, LanguageRepository}
import ro.andonescu.scala.startup.models.entity.Film
import ro.andonescu.scala.startup.validations.FilmValidation
import ro.andonescu.scala.startup.validations.errors.ErrorMessage

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/26/2016.
 */
trait FilmsService {
  def findAll(): Future[Seq[Film]]

  def save(f: FilmForm): Future[Either[Seq[ErrorMessage], Long]]

  def delete(id: Int): Future[Either[Seq[ErrorMessage], Int]]

  def update(id: Long, f: FilmForm): Future[Either[Seq[ErrorMessage], Long]]
}

@Singleton
class FilmsServiceImpl @Inject() (repo: FilmRepository, validation: FilmValidation)(implicit ec: ExecutionContext) extends FilmsService {
  override def findAll(): Future[Seq[Film]] = {
    repo.findAll()
  }

  override def save(f: FilmForm): Future[Either[Seq[ErrorMessage], Long]] = {

    validation.saveValidation(f).flatMap {
      case Nil =>
        repo.save(Film(0, f.title, f.description, f.releaseYear, f.languageId, f.originalLanguageId, f.rentalDuration, f.rentalRate, f.length, f.replacementCost, f.rating)).map((v => Right(v)))
      case errors =>
        Future.successful(Left(errors))
    }
  }

  override def delete(id: Int): Future[Either[Seq[ErrorMessage], Int]] = {
    validation.idValid(id).flatMap {
      case Nil   => repo.delete(id).map(v => Right(v))
      case error => Future.successful(Left(error))
    }
  }

  override def update(id: Long, f: FilmForm): Future[Either[Seq[ErrorMessage], Long]] = {
    validation.updateValidation(f, id).flatMap {
      case Nil =>
        repo.update(Film(id, f.title, f.description, f.releaseYear, f.languageId, f.originalLanguageId, f.rentalDuration, f.rentalRate, f.length, f.replacementCost, f.rating)).map((v => Right(v)))
      case errors =>
        Future.successful(Left(errors))
    }
  }
}
