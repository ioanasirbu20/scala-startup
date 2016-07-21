package ro.andonescu.scala.startup.controllers.views

import play.api.libs.json.Json
import ro.andonescu.scala.startup.models.entity.Language

case class LanguagesView(items: Seq[Language])

object LanguagesView {
  implicit val languagesViewFormat = Json.format[LanguagesView]
}