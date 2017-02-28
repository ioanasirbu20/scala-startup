package ro.andonescu.scala.startup.models

import com.google.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import ro.andonescu.scala.startup.models.entity.Login
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LoginRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class LoginTable(tag: Tag) extends Table[Login](tag, "login") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def username = column[String]("username")

    def password = column[String]("password")

    def * = (id, username, password) <> ((Login.apply _).tupled, Login.unapply)
  }

  private val login = TableQuery[LoginTable]

  def save(l: Login): Future[Long] = db.run {
    login returning login.map(_.id) += l
  }

  def byUsername(username: String): Future[Option[Login]] = db.run {
    login.filter(_.username === username).result.headOption
  }
}
