package ro.andonescu.scala.startup.services

import java.util.Calendar

import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime
import ro.andonescu.scala.startup.controllers.jsons.{FilmActorView, FilmCategoryView, FilmForm, FilmWithActor}
import ro.andonescu.scala.startup.models._
import ro.andonescu.scala.startup.models.entity._
import ro.andonescu.scala.startup.validations.FilmValidation
import ro.andonescu.scala.startup.validations.errors.ErrorMessage
import slick.jdbc.meta.MBestRowIdentifierColumn.Scope.Transaction

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/26/2016.
 */
trait FilmsService {
  def findAll(): Future[Seq[FilmWithActor]]

  def save(f: FilmForm): Future[Either[Seq[ErrorMessage], Long]]

  def delete(id: Int): Future[Either[Seq[ErrorMessage], Int]]

  def update(id: Long, f: FilmForm): Future[Either[Seq[ErrorMessage], Long]]
}

@Singleton
class FilmsServiceImpl @Inject() (repo: FilmRepository, repoFilmActor: FilmActorRepository, repoActor: ActorRepository, repoCategory: CategoryRepository, repoFilmCategory: FilmCategoryRepository, validation: FilmValidation)(implicit ec: ExecutionContext) extends FilmsService {
  override def findAll(): Future[Seq[FilmWithActor]] = {
    for {

      // filme
      films <- repo.findAll()
      filmIds = films.map(_.id)

      // film actors
      filmActors <- repoFilmActor.findByFilmIds(filmIds)

      actorsIds = filmActors.map(_.actorId)
      //actors
      actors <- repoActor.findById(actorsIds)

      // filme (id) cu film_actors (id)
      filmsWithActors = filmActors.groupBy(_.filmId)

      category <- repoCategory.findAll()
      categoryId = category.map(_.id)

      filmsCategories <- repoFilmCategory.findByFilmIds(filmIds)
      filmsWithCategories = filmsCategories.groupBy(_.filmId)

      filmsAndActors: Seq[FilmWithActor] = aggregateFilmsAndActors(films, actors, category, filmsWithActors, filmsWithCategories)
    } yield filmsAndActors

  }

  private def aggregateFilmsAndActors(films: Seq[Film], actors: Seq[Actor], category: Seq[Category], filmsWithActors: Map[Long, Seq[FilmActor]], filmsWithCategories: Map[Long, Seq[FilmCategory]]): Seq[FilmWithActor] = {

    films.map { film =>

      val filmActors = filmsWithActors.filter(p => p._1 == film.id).headOption.map(p => p._2.map(_.actorId)).getOrElse(Seq.empty[Long])

      val actorsObj = actors.filter(a => filmActors.contains(a.id)).map(a => FilmActorView(a.id, a.firstName, a.lastName))

      val filmCategory = filmsWithCategories.filter(p => p._1 == film.id).headOption.map(p => p._2.map(_.categoryId)).getOrElse(Seq.empty[Long])

      val categoryObj = category.filter(c => filmCategory.contains(c.id)).map(c => FilmCategoryView(c.name))

      FilmWithActor(film.id, film.title, film.description, film.releaseYear, film.languageId, film.originalLanguageId, actorsObj, categoryObj, film.rentalDuration, film.rentalRate, film.length, film.replacementCost, film.rating)

    }
  }

  override def save(f: FilmForm): Future[Either[Seq[ErrorMessage], Long]] = {

    validation.saveValidation(f).flatMap {
      case Nil => {
        val filmIdF = repo.save(Film(0, f.title, f.description, f.releaseYear, f.languageId, f.originalLanguageId, f.rentalDuration, f.rentalRate, f.length, f.replacementCost, f.rating))
        for {
          filmId <- filmIdF
          filmActor <- repoFilmActor.saveAll(f.actors.map(id => FilmActor(id, filmId, DateTime.now())))
          _ <- repoFilmCategory.saveAll(f.category.map(id => FilmCategory(filmId, id, DateTime.now())))
        } yield filmId
      }.map((v => Right(v)))
      case errors =>
        Future.successful(Left(errors))
    }
  }

  override def delete(id: Int): Future[Either[Seq[ErrorMessage], Int]] = {

    validation.idValid(id).flatMap {
      case Nil => {
        for {
          _ <- repoFilmActor.deleteByFilmId(id)
          deleteFilmCategory <- repoFilmCategory.deleteByFilmId(id)
          deleteFilm <- repo.delete(id)

        } yield deleteFilm

      }.map(v => Right(v))
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
