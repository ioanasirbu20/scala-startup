package ro.andonescu.scala.startup.controllers.jsons

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import ro.andonescu.scala.startup.models.entity.{Actor, Film, FilmActor}

/**
  * Created by V3790155 on 7/25/2016.
  */

case class FilmForm(title: String, description: String, releaseYear: Int,
                    languageId: Int, originalLanguageId: Int, actors: Seq[Long], rentalDuration: Int,
                    rentalRate: Float, length: Int, replacementCost: Float, rating: String)

case class FilmsView(items: Seq[FilmWithActor])
case class FilmsCategoryView(items: Seq[FilmWithCategory])

case class FilmWithActor(id: Long, title: String, description: String, releaseYear: Int,
                         languageId: Int, originalLanguageId: Int, actors: Seq[FilmActorView], rentalDuration: Int,
                         rentalRate: Float, length: Int, replacementCost: Float, rating: String)

case class FilmWithCategory(id: Long, title: String, description: String, releaseYear: Int,
                            languageId: Int, originalLanguageId: Int, category: Seq[FilmCategoryView], rentalDuration: Int,
                            rentalRate: Float, length: Int, replacementCost: Float, rating: String)

case class FilmActorView(id: Long, firstName: String, lastName: String)

case class FilmCategoryView(name: String)

object FilmCategoryView {
  implicit object FilmCategoryWrites extends Writes[FilmCategoryView] {
    override def writes(fc: FilmCategoryView): JsValue = Json.obj(
      "name" -> Json.toJson(fc.name)
    )
  }
  implicit val filmCategoryViewWrites = Json.writes[FilmCategoryView]
}

object FilmWithCategory {
  implicit object FilmWithCategoryWrites extends Writes[FilmWithCategory] {
    override def writes(fc: FilmWithCategory): JsValue = Json.obj(
      "id" -> Json.toJson(fc.id),
      "title" -> Json.toJson(fc.title),
      "description" -> Json.toJson(fc.description),
      "releaseYear" -> Json.toJson(fc.releaseYear),
      "languageId" -> Json.toJson(fc.languageId),
      "originalLanguageId" -> Json.toJson(fc.originalLanguageId),
      "category" -> Json.toJson(fc.category),
      "rentalDuration" -> Json.toJson(fc.rentalDuration),
      "rentalRate" -> Json.toJson(fc.rentalRate),
      "length" -> Json.toJson(fc.length),
      "replacementCost" -> Json.toJson(fc.replacementCost),
      "rating" -> Json.toJson(fc.rating)
    )
  }
}
object FilmForm {
  implicit val filmFormReads: Reads[FilmForm] = (
    (JsPath \ "title").read[String](minLength[String](3)) and
      (JsPath \ "description").read[String](minLength[String](20)) and
      (JsPath \ "releaseYear").read[Int] and
      (JsPath \ "languageId").read[Int] and
      (JsPath \ "originalLanguageId").read[Int] and
      (JsPath \ "actors").read[Seq[Long]] and
      (JsPath \ "rentalDuration").read[Int] and
      (JsPath \ "rentalRate").read[Float] and
      (JsPath \ "length").read[Int] and
      (JsPath \ "replacementCost").read[Float] and
      (JsPath \ "rating").read[String]
    ) (FilmForm.apply _)
}

object FilmActorView {

  implicit object FilmActorWrites extends Writes[FilmActorView] {
    override def writes(f: FilmActorView): JsValue = Json.obj(
      "id" -> Json.toJson(f.id),
      "lastName" -> Json.toJson(f.lastName),
      "firstName" -> Json.toJson(f.firstName)
    )
  }

}

object FilmWithActor {

  implicit object FilmWithActorWrites extends Writes[FilmWithActor] {
    override def writes(f: FilmWithActor): JsValue = Json.obj(
      "id" -> Json.toJson(f.id),
      "title" -> Json.toJson(f.title),
      "description" -> Json.toJson(f.description),
      "releaseYear" -> Json.toJson(f.releaseYear),
      "languageId" -> Json.toJson(f.languageId),
      "originalLanguageId" -> Json.toJson(f.originalLanguageId),
      "actors" -> Json.toJson(f.actors),
      "rentalDuration" -> Json.toJson(f.rentalDuration),
      "rentalRate" -> Json.toJson(f.rentalRate),
      "length" -> Json.toJson(f.length),
      "replacementCost" -> Json.toJson(f.replacementCost),
      "rating" -> Json.toJson(f.rating)
    )
  }

}

object FilmsView {
  implicit val filmViewWrites = Json.writes[FilmsView]
}