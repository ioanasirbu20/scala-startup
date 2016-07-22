package ro.andonescu.scala.startup.controllers.jsons

import play.api.libs.json.{JsValue, Json, Writes}
import ro.andonescu.scala.startup.models.entity.Language

case class LanguagesView(items: Seq[Language])

object LanguagesView {

  implicit object LanguageWrites extends Writes[Language] {
    override def writes(l: Language): JsValue = Json.obj(
      "id" -> Json.toJson(l.id),
      "name" -> Json.toJson(l.name)
    )
  }

  implicit val languagesViewFormat = Json.writes[LanguagesView]
}

