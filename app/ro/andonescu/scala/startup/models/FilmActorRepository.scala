package ro.andonescu.scala.startup.models

import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import ro.andonescu.scala.startup.models.entity.FilmActor
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 8/1/2016.
 */
@Singleton
class FilmActorRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._
  import com.github.tototoshi.slick.PostgresJodaSupport._

  private class FilmActorTable(tag: Tag) extends Table[FilmActor](tag, "filmActor") {
    def actorId = column[Long]("actorId")

    def filmId = column[Long]("filmId")

    def lastUpdate = column[DateTime]("lastUpdate")

    def * = (actorId, filmId, lastUpdate) <> ((FilmActor.apply _).tupled, FilmActor.unapply)
  }

  private val filmActor = TableQuery[FilmActorTable]

  def save(fa: FilmActor): Future[Long] = db.run {
    filmActor returning filmActor.map(_.actorId) += fa
  }

  def saveAll(fas: Seq[FilmActor]): Future[Seq[Long]] = db.run {
    filmActor returning filmActor.map(_.actorId) ++= fas
  }

  def findByFilmIds(ids: Seq[Long]): Future[Seq[FilmActor]] = db.run {
    filmActor.filter(_.filmId inSet ids).result
  }

  def findByActorIds(ids: Seq[Long]): Future[Seq[FilmActor]] = db.run {
    filmActor.filter(_.actorId inSet ids).result
  }

  def deleteByFilmId(id: Long): Future[Int] = db.run {
    filmActor.filter(_.filmId === id).delete
  }

  def deleteByActorId(id: Long): Future[Int] = db.run {
    filmActor.filter(_.actorId === id).delete
  }
}

