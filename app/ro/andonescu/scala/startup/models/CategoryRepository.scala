package ro.andonescu.scala.startup.models

import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import ro.andonescu.scala.startup.models.entity.Category
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 8/9/2016.
 */
@Singleton
class CategoryRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._
  import com.github.tototoshi.slick.PostgresJodaSupport._

  private class CategoryTable(tag: Tag) extends Table[Category](tag, "category") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def lastUpdate = column[DateTime]("last_update")

    def * = (id, name, lastUpdate) <> ((Category.apply _).tupled, Category.unapply)
  }

  private val category = TableQuery[CategoryTable]

  def save(c: Category): Future[Long] = db.run {
    category returning category.map(_.id) += c
  }

  def findAll(): Future[Seq[Category]] = db.run {
    category.result
  }

  def byName(name: String): Future[Option[Category]] = db.run {
    category.filter(_.name === name).result.headOption
  }

  def existIds(ids: Seq[Long]): Future[Seq[Long]] = db.run {
    category.filter(_.id inSet ids).map(_.id).result
  }

}
