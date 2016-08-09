package ro.andonescu.scala.startup.controllers.jsons

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import ro.andonescu.scala.startup.models.entity.Category

/**
 * Created by V3790155 on 8/9/2016.
 */
case class CategoryView(items: Seq[Category])

case class CategoryForm(name: String)

object CategoryForm {
  implicit val categoryFormReads: Reads[CategoryForm] = (JsPath \ "name").read[String].map(CategoryForm(_))
}

object CategoryView {

  implicit object CategoryViewWrites extends Writes[Category] {
    override def writes(c: Category): JsValue = Json.obj(
      "id" -> Json.toJson(c.id),
      "name" -> Json.toJson(c.name)
    )
  }

  implicit val categoryViewFormat = Json.writes[CategoryView]
}

