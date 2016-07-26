package ro.andonescu.scala.startup.services

import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime
import ro.andonescu.scala.startup.controllers.jsons.ActorForm
import ro.andonescu.scala.startup.models.ActorRepository
import ro.andonescu.scala.startup.models.entity.Actor

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/26/2016.
 */
trait ActorsService {

  def save(actorForm: ActorForm): Future[Either[String, Long]]

  def findAll(): Future[Seq[Actor]]

  def delete(id: Long): Future[Int]

}

@Singleton
class ActorsServiceImpl @Inject() (repo: ActorRepository)(implicit ec: ExecutionContext) extends ActorsService {
  override def findAll(): Future[Seq[Actor]] = {
    repo.findAll()
  }

  override def save(actorForm: ActorForm): Future[Either[String, Long]] = {

    def isActorValid(): Future[Boolean] = {
      repo.by(actorForm.firstName, actorForm.lastName).map {
        case Some(_) => false
        case None    => true
      }
    }

    isActorValid().flatMap { isValid =>
      if (isValid)
        repo.save(Actor(0, actorForm.firstName, actorForm.lastName, DateTime.now())).map(v => Right(v))
      else
        Future.successful(Left("Actor already saved!"))
    }
  }

  override def delete(id: Long): Future[Int] = {
    repo.delete(id)
  }
}
