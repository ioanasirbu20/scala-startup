package ro.andonescu.scala.startup.services

import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime
import ro.andonescu.scala.startup.controllers.jsons.{ActorFilmView, ActorForm, ActorsWithFilms, FilmWithActor}
import ro.andonescu.scala.startup.models.{ActorRepository, FilmActorRepository, FilmRepository}
import ro.andonescu.scala.startup.models.entity.{Actor, Film, FilmActor}
import ro.andonescu.scala.startup.validations.errors.{ErrorMessage, ErrorMessages}

import scala.concurrent.{ExecutionContext, Future}

trait ActorsService {

  def save(actorForm: ActorForm): Future[Either[Seq[ErrorMessage], Long]]

  def findAll(): Future[Seq[ActorsWithFilms]]

  def delete(id: Long): Future[Int]

}

@Singleton
class ActorsServiceImpl @Inject() (repo: ActorRepository, repoFilm: FilmRepository, repoFilmActor: FilmActorRepository)(implicit ec: ExecutionContext) extends ActorsService {
  override def findAll(): Future[Seq[ActorsWithFilms]] = {
    for {
      actors <- repo.findAll()
      actorsIds = actors.map(_.id)

      films <- repoFilm.findAll()

      actorsFilms <- repoFilmActor.findByActorIds(actorsIds)
      actorsWithFilms = actorsFilms.groupBy(_.actorId)

      actorsAndFilms: Seq[ActorsWithFilms] = aggregateActorsAndFilms(films, actors, actorsWithFilms)
    } yield actorsAndFilms
  }

  def aggregateActorsAndFilms(films: Seq[Film], actors: Seq[Actor], actorsWithFilms: Map[Long, Seq[FilmActor]]): Seq[ActorsWithFilms] = {
    actors.map { actor =>

      val actorsFilms = actorsWithFilms.getOrElse(actor.id, Seq.empty[FilmActor]).map(_.filmId)

      val filmsObj = films.filter(f => actorsFilms.contains(f.id)).map(f => ActorFilmView(f.id, f.title, f.description, f.releaseYear))

      ActorsWithFilms(actor.id, actor.firstName, actor.lastName, filmsObj)
    }
  }

  override def save(actorForm: ActorForm): Future[Either[Seq[ErrorMessage], Long]] = {
    validateActorCreate(actorForm).flatMap {
      case Nil =>
        repo.save(Actor(0, actorForm.firstName, actorForm.lastName)).map(v => Right(v))
      case errors => Future.successful(Left(errors))
    }
  }

  def validateActorCreate(actorForm: ActorForm): Future[Seq[ErrorMessage]] = {
    def isActorValid(): Future[Seq[ErrorMessage]] = {
      repo.by(actorForm.firstName, actorForm.lastName).map {
        case Some(_) => Seq(ErrorMessage("firstName, lastName", "Actor already saved."))
        case None    => Seq.empty
      }
    }
    //
    //    val b : Seq[ErrorMessage] = ???
    //    val c: Future[Seq[ErrorMessage]] = ???
    //
    //
    //     for {
    //      actorValidSeq <- isActorValid()
    //      cSeq <- c
    //    } yield actorValidSeq ++ cSeq ++ b
    //
    isActorValid()
  }

  override def delete(id: Long): Future[Int] = {
    for {
      _ <- repoFilmActor.deleteByActorId(id)
      deleteActor <- repo.delete(id)
    } yield deleteActor

  }
}
