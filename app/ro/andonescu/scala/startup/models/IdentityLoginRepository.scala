package ro.andonescu.scala.startup.models

import com.google.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import ro.andonescu.scala.startup.models.entity.IdentityLogin
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class IdentityLoginRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class IdentityLoginTable(tag: Tag) extends Table[IdentityLogin](tag, "identity_login") {
    def identityId = column[Long]("identity_id")

    def loginId = column[Long]("login_id")

    def * = (identityId, loginId) <> ((IdentityLogin.apply _).tupled, IdentityLogin.unapply)
  }

  private val identityLogin = TableQuery[IdentityLoginTable]

  def save(il: IdentityLogin): Future[Long] = db.run {
    identityLogin returning identityLogin.map(_.loginId) += il
  }
}
