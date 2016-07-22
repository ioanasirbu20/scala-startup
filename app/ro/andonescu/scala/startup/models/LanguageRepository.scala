package ro.andonescu.scala.startup.models

import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import ro.andonescu.scala.startup.models.entity.Language
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/21/2016.
 */
@Singleton
class LanguageRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._
  import com.github.tototoshi.slick.PostgresJodaSupport._

  private class LanguageTable(tag: Tag) extends Table[Language](tag, "language") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def lastUpdate = column[DateTime]("lastUpdate")

    def * = (id, name, lastUpdate) <> ((Language.apply _).tupled, Language.unapply)
  }

  private val language = TableQuery[LanguageTable]

  def findAll(): Future[Seq[Language]] = db.run {
    language.result
  }

}
