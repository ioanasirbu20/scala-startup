package ro.andonescu.scala.startup.models.entity

import play.api.libs.json.Json

case class Login(id: Long, username: String, password: String)

object Login {
  implicit val loginFormat = Json.format[Login]
}