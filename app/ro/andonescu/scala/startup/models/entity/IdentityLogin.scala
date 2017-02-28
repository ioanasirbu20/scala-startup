package ro.andonescu.scala.startup.models.entity

import play.api.libs.json.Json

case class IdentityLogin(identityId: Long, loginId: Long)

object IdentityLogin {
  implicit val identityLoginFormat = Json.format[IdentityLogin]
}