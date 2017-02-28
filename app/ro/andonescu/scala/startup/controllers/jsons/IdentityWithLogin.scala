package ro.andonescu.scala.startup.controllers.jsons

import play.api.libs.json.{JsValue, Json, Writes}

case class IdentityWithLogin(firstName: String, lastName: String, email: String, username: String, password: String)

object IdentityWithLogin {
  implicit val identityWithLoginReads = Json.reads[IdentityWithLogin]
}
