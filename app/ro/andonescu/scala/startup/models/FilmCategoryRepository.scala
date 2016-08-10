package ro.andonescu.scala.startup.models

import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import ro.andonescu.scala.startup.models.entity.FilmCategory
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 8/9/2016.
 */
@Singleton
class FilmCategoryRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._
  import com.github.tototoshi.slick.PostgresJodaSupport._

  private class FilmCategoryTable(tag: Tag) extends Table[FilmCategory](tag, "film_category") {
    def filmId = column[Long]("film_id")

    def categoryId = column[Long]("category_id")

    def lastUpdate = column[DateTime]("last_update")

    def * = (filmId, categoryId, lastUpdate) <> ((FilmCategory.apply _).tupled, FilmCategory.unapply)
  }

  private val filmCategory = TableQuery[FilmCategoryTable]

  def save(fc: FilmCategory): Future[Long] = db.run {
    filmCategory returning filmCategory.map(_.categoryId) += fc
  }

  def saveAll(fcs: Seq[FilmCategory]): Future[Seq[Long]] = db.run {
    filmCategory returning filmCategory.map(_.categoryId) ++= fcs
  }

  def deleteByFilmId(id: Long): Future[Int] = db.run {
    filmCategory.filter(_.filmId === id).delete
  }

  //  def deleteByCategoryId(id: Long): Future[Int] = db.run {
  //    filmCategory.filter(_.categoryId === id).delete
  //  }

  def findByFilmIds(ids: Seq[Long]): Future[Seq[FilmCategory]] = db.run {
    filmCategory.filter(_.filmId inSet ids).result
  }
}
