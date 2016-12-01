package ro.andonescu.scala.startup.controllers.jsons

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import ro.andonescu.scala.startup.models.entity.{Actor, Film, FilmActor}

/**
 * Created by V3790155 on 7/25/2016.
 */

case class FilmForm(title: String, description: String, releaseYear: Int,
  languageId: Int, actors: Seq[Long], category: Seq[Long], rating: String)

case class FilmsView(items: Seq[FilmWithActor])

case class FilmsCategoryView(items: Seq[FilmWithCategory])

case class FilmWithActor(id: Long, title: String, description: String, releaseYear: Int,
  languageId: Int, actors: Seq[FilmActorView], category: Seq[FilmCategoryView], rating: String)

case class FilmWithCategory(id: Long, title: String, description: String, releaseYear: Int,
  languageId: Int, category: Seq[FilmCategoryView], rating: String)

case class FilmActorView(id: Long, firstName: String, lastName: String)

case class FilmCategoryView(name: String)

object FilmCategoryView {

  implicit object FilmCategoryWrites extends Writes[FilmCategoryView] {
    override def writes(fc: FilmCategoryView): JsValue = Json.obj(
      "name" -> Json.toJson(fc.name)
    )
  }

  //implicit val filmCategoryViewWrites = Json.writes[FilmCategoryView]
}

object FilmWithCategory {

  implicit object FilmWithCategoryWrites extends Writes[FilmWithCategory] {
    override def writes(fc: FilmWithCategory): JsValue = Json.obj(
      "id" -> Json.toJson(fc.id),
      "title" -> Json.toJson(fc.title),
      "description" -> Json.toJson(fc.description),
      "releaseYear" -> Json.toJson(fc.releaseYear),
      "languageId" -> Json.toJson(fc.languageId),
      "category" -> Json.toJson(fc.category),
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
    (JsPath \ "actors").read[Seq[Long]] and
    (JsPath \ "category").read[Seq[Long]] and
    (JsPath \ "rating").read[String]
  )(FilmForm.apply _)
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
      "actors" -> Json.toJson(f.actors),
      "category" -> Json.toJson(f.category),
      "rating" -> Json.toJson(f.rating)
    )
  }

}

object FilmsView {
  implicit val filmViewWrites = Json.writes[FilmsView]
}