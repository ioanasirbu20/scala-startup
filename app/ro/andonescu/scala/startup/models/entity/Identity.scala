package ro.andonescu.scala.startup.models.entity

import play.api.libs.json.Json

case class Identity(id: Long, firstName: String, lastName: String, email: String)

object Identity {
  implicit val identityFormat = Json.format[Identity]
}