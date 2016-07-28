package ro.andonescu.scala.startup.controllers.jsons

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import ro.andonescu.scala.startup.models.entity.Film

/**
 * Created by V3790155 on 7/25/2016.
 */

case class FilmForm(title: String, description: String, releaseYear: Int,
  languageId: Int, originalLanguageId: Int, rentalDuration: Int,
  rentalRate: Float, length: Int, replacementCost: Float, rating: String)

case class FilmsView(items: Seq[Film])

object FilmsView {

  implicit object FilmWrites extends Writes[Film] {
    override def writes(f: Film): JsValue = Json.obj(
      "id" -> Json.toJson(f.id),
      "title" -> Json.toJson(f.title),
      "description" -> Json.toJson(f.description),
      "releaseYear" -> Json.toJson(f.releaseYear),
      "languageId" -> Json.toJson(f.languageId),
      "originalLanguageId" -> Json.toJson(f.originalLanguageId),
      "rentalDuration" -> Json.toJson(f.rentalDuration),
      "rentalRate" -> Json.toJson(f.rentalRate),
      "length" -> Json.toJson(f.length),
      "replacementCost" -> Json.toJson(f.replacementCost),
      "rating" -> Json.toJson(f.rating)
    )
  }

  implicit val filmViewWrites = Json.writes[FilmsView]

}

object FilmForm {
  implicit val filmFormReads: Reads[FilmForm] = (
    (JsPath \ "title").read[String](minLength[String](3)) and
    (JsPath \ "description").read[String](minLength[String](20)) and
    (JsPath \ "releaseYear").read[Int] and
    (JsPath \ "languageId").read[Int] and
    (JsPath \ "originalLanguageId").read[Int] and
    (JsPath \ "rentalDuration").read[Int] and
    (JsPath \ "rentalRate").read[Float] and
    (JsPath \ "length").read[Int] and
    (JsPath \ "replacementCost").read[Float] and
    (JsPath \ "rating").read[String]
  )(FilmForm.apply _)
}