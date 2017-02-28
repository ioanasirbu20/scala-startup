package ro.andonescu.scala.startup.models

import com.google.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import ro.andonescu.scala.startup.models.entity.Identity
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class IdentityRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class IdentityTable(tag: Tag) extends Table[Identity](tag, "identity") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def firstName = column[String]("first_name")

    def lastName = column[String]("last_name")

    def email = column[String]("email")

    def * = (id, firstName, lastName, email) <> ((Identity.apply _).tupled, Identity.unapply)
  }

  private val identity = TableQuery[IdentityTable]

  def findAll: Future[Seq[Identity]] = db.run {
    identity.result
  }

  def save(i: Identity): Future[Long] = db.run {
    identity returning identity.map(_.id) += i
  }

  def countByEmail(email: String): Future[Int] = db.run {
    identity.filter(_.email === email).countDistinct.result
  }

}
