package ro.andonescu.scala.startup.validations.errors

import play.api.libs.json._

/**
 * Created by V3790155 on 7/28/2016.
 */
case class ErrorMessages(endpoint: String, errors: Seq[ErrorMessage])

case class ErrorMessage(field: String, message: String)

object ErrorMessages {
  implicit val errorWrites = Json.writes[ErrorMessage]
  implicit val errorMessagesWrites = Json.writes[ErrorMessages]
}