package ro.andonescu.scala.startup.models

import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import ro.andonescu.scala.startup.models.entity.Actor
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ActorRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._
  import com.github.tototoshi.slick.PostgresJodaSupport._

  private class ActorTable(tag: Tag) extends Table[Actor](tag, "actor") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def firstName = column[String]("firstName")

    def lastName = column[String]("lastName")

    def * = (id, firstName, lastName) <> ((Actor.apply _).tupled, Actor.unapply)
  }

  private val actor = TableQuery[ActorTable]

  def findAll(): Future[Seq[Actor]] = db.run {
    actor.result
  }

  def findById(ids: Seq[Long]): Future[Seq[Actor]] = db.run {
    actor.filter(_.id inSet ids).result
  }

  def save(a: Actor): Future[Long] = db.run {
    actor returning actor.map(_.id) += a
  }

  def delete(id: Long): Future[Int] = db.run {
    actor.filter(_.id === id).delete
  }

  def by(firstName: String, lastName: String): Future[Option[Actor]] = db.run {
    actor.filter(a => a.lastName === lastName && a.firstName === firstName).result.headOption
  }

  def existsId(ids: Seq[Long]): Future[Seq[Long]] = db.run {
    actor.filter(_.id inSet ids).map(_.id).result
  }

}
