package ro.andonescu.scala.startup.services

import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime
import ro.andonescu.scala.startup.controllers.jsons.ActorForm
import ro.andonescu.scala.startup.models.ActorRepository
import ro.andonescu.scala.startup.models.entity.Actor
import ro.andonescu.scala.startup.validations.errors.{ErrorMessage, ErrorMessages}

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/26/2016.
 */
trait ActorsService {

  def save(actorForm: ActorForm): Future[Either[Seq[ErrorMessage], Long]]

  def findAll(): Future[Seq[Actor]]

  def delete(id: Long): Future[Int]

}

@Singleton
class ActorsServiceImpl @Inject() (repo: ActorRepository)(implicit ec: ExecutionContext) extends ActorsService {
  override def findAll(): Future[Seq[Actor]] = {
    repo.findAll()
  }

  override def save(actorForm: ActorForm): Future[Either[Seq[ErrorMessage], Long]] = {
    validateActorCreate(actorForm).flatMap {
      case Nil =>
        repo.save(Actor(0, actorForm.firstName, actorForm.lastName, DateTime.now())).map(v => Right(v))
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
    repo.delete(id)
  }
}
