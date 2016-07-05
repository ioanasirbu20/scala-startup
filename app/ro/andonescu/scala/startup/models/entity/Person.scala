package ro.andonescu.scala.startup.models.entity

import play.api.libs.json._

case class Person(id: Long, name: String, age: Int)

object Person {

  implicit val personFormat = Json.format[Person]
}
