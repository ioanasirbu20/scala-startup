package ro.andonescu.scala.startup.controllers.jsons

import play.api.libs.json.Json

case class LoginForm(username: String, password: String)

object LoginForm {
  implicit val loginFormFormat = Json.format[LoginForm]
}
