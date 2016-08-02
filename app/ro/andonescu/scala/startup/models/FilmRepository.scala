package ro.andonescu.scala.startup.models

import com.google.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import ro.andonescu.scala.startup.controllers.jsons.FilmForm
import ro.andonescu.scala.startup.models.entity.Film
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/25/2016.
 */
@Singleton
class FilmRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class FilmTable(tag: Tag) extends Table[Film](tag, "film") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("title")

    def description = column[String]("description")

    def releaseYear = column[Int]("releaseYear")

    def languageId = column[Int]("languageId")

    def originalLanguageId = column[Int]("originalLanguageId")

    def rentalDuration = column[Int]("rentalDuration")

    def rentalRate = column[Float]("rentalRate")

    def length = column[Int]("length")

    def replacementCost = column[Float]("replacementCost")

    def rating = column[String]("rating")

    def * = (id, title, description, releaseYear, languageId, originalLanguageId, rentalDuration, rentalRate, length, replacementCost, rating) <> ((Film.apply _).tupled, Film.unapply)
  }

  private val film = TableQuery[FilmTable]

  def findAll(): Future[Seq[Film]] = db.run {
    film.result
  }

  def save(f: Film): Future[Long] = db.run {
    film returning film.map(_.id) += f
  }

  def byTitle(title: String): Future[Option[Film]] = db.run {
    film.filter(_.title === title).result.headOption
  }

  def delete(id: Long): Future[Int] = db.run {
    film.filter(_.id === id).delete
  }

  def update(f: Film) = db.run {
    film.filter(_.id === f.id).update(f)
  }

  def existsByTitleAndNotId(title: String, notId: Long): Future[Boolean] = db.run {
    film.filter(f => f.id =!= notId && f.title === title).exists.result
  }

  def existsById(id: Long): Future[Boolean] = db.run {
    film.filter(_.id === id).exists.result
  }

  //  def getIds(): Future[Seq[Long]] = db.run {
  //    film.map(_.id).result
  //  }
}
