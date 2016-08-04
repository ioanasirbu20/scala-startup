package ro.andonescu.scala.startup.services

import java.util.Calendar

import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime
import ro.andonescu.scala.startup.controllers.jsons.{FilmActorView, FilmForm, FilmWithActor}
import ro.andonescu.scala.startup.models.{ActorRepository, FilmActorRepository, FilmRepository, LanguageRepository}
import ro.andonescu.scala.startup.models.entity.{Actor, Film, FilmActor}
import ro.andonescu.scala.startup.validations.FilmValidation
import ro.andonescu.scala.startup.validations.errors.ErrorMessage

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
class FilmsServiceImpl @Inject()(repo: FilmRepository, repoFilmActor: FilmActorRepository, repoActor: ActorRepository, validation: FilmValidation)(implicit ec: ExecutionContext) extends FilmsService {
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

      filmsAndActors: Seq[FilmWithActor] = aggregateFilmsAndActors(films, actors, filmsWithActors)
    } yield filmsAndActors

  }

  private def aggregateFilmsAndActors(films: Seq[Film], actors: Seq[Actor], filmsWithActors: Map[Long, Seq[FilmActor]]): Seq[FilmWithActor] = {

    var filmAndActor: Seq[FilmWithActor] = Seq.empty
    var actorsSeq: Seq[FilmActorView] = Seq.empty

    def getActors(id: Long): Seq[FilmActorView] = {
      var filmActorView: Seq[FilmActorView] = Seq.empty
      for (i <- 0 until actors.length) {
        if (actors(i).id == id)
          filmActorView = filmActorView :+ FilmActorView(actors(i).id, actors(i).firstName, actors(i).lastName)
      }
      filmActorView
    }

    def getActorIds(id: Long): Seq[Long] = {
      val keys = filmsWithActors.keys.toSeq
      var actorSeq: Seq[Long] = Seq.empty

      for (i <- 0 until filmsWithActors.size)
        if (id == keys(i)) {
          actorSeq = filmsWithActors(id).map(_.actorId)
        }
      actorSeq
    }

    for (i <- 0 until films.length) {
      if (getActorIds(films(i).id) == Seq.empty) {
        filmAndActor = filmAndActor :+ FilmWithActor(films(i).id, films(i).title, films(i).description, films(i).releaseYear, films(i).languageId, films(i).originalLanguageId, Seq.empty, films(i).rentalDuration, films(i).rentalRate, films(i).length, films(i).replacementCost, films(i).rating)
      } else {
        getActorIds(films(i).id).map { actor =>
          actorsSeq = actorsSeq ++ getActors(actor)
        }
        filmAndActor = filmAndActor :+ FilmWithActor(films(i).id, films(i).title, films(i).description, films(i).releaseYear, films(i).languageId, films(i).originalLanguageId, actorsSeq, films(i).rentalDuration, films(i).rentalRate, films(i).length, films(i).replacementCost, films(i).rating)
        actorsSeq = Seq.empty
      }
    }
    filmAndActor
  }

  override def save(f: FilmForm): Future[Either[Seq[ErrorMessage], Long]] = {

    validation.saveValidation(f).flatMap {
      case Nil => {
        val filmIdF = repo.save(Film(0, f.title, f.description, f.releaseYear, f.languageId, f.originalLanguageId, f.rentalDuration, f.rentalRate, f.length, f.replacementCost, f.rating))
        for {
          filmId <- filmIdF
          _ <- repoFilmActor.saveAll(f.actors.map(id => FilmActor(id, filmId, DateTime.now())))
        } yield filmId
      }.map((v => Right(v)))
      case errors =>
        Future.successful(Left(errors))
    }
  }

  override def delete(id: Int): Future[Either[Seq[ErrorMessage], Int]] = {
    validation.idValid(id).flatMap {
      case Nil => repo.delete(id).map(v => Right(v))
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
