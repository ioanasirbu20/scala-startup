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

  def save(actorForm: ActorForm): Future[Long]

  def findAll(): Future[Seq[Actor]]

  def delete(id: Long): Future[Int]

}

@Singleton
class ActorsServiceImpl @Inject() (repo: ActorRepository)(implicit ec: ExecutionContext) extends ActorsService {
  override def findAll(): Future[Seq[Actor]] = {
    repo.findAll()
  }

  override def save(actorForm: ActorForm): Future[Long] = {
    repo.save(Actor(0, actorForm.firstName, actorForm.lastName, DateTime.now()))
  }

  override def delete(id: Long): Future[Int] = {
    repo.delete(id)
  }

  private def hello = "Hello"

}
