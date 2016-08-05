package ro.andonescu.scala.startup.controllers.jsons

import play.api.libs.json._
import ro.andonescu.scala.startup.models.entity.Actor
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

/**
  * Created by V3790155 on 7/22/2016.
  */
case class ActorForm(firstName: String, lastName: String)

case class ActorsView(items: Seq[ActorsWithFilms])

case class ActorsWithFilms(id: Long, firstName: String, lastName: String, films: Seq[ActorFilmView])

case class ActorFilmView(id: Long, title: String, description: String, releaseYear: Int)

object ActorForm {
  implicit val actorFormReads: Reads[ActorForm] = (
    (JsPath \ "firstName").read[String](minLength[String](2)) and
      (JsPath \ "lastName").read[String](minLength[String](5))
    ) (ActorForm.apply _)
}

object ActorFilmView {

  implicit object ActorFilmWrites extends Writes[ActorFilmView] {
    override def writes(a: ActorFilmView): JsValue = Json.obj(
      "id" -> Json.toJson(a.id),
      "title" -> Json.toJson(a.title),
      "description" -> Json.toJson(a.description),
      "releaseYear" -> Json.toJson(a.releaseYear)
    )
  }

}

object ActorsWithFilms {

  implicit object ActorsWithFilmsWrites extends Writes[ActorsWithFilms] {
    override def writes(a: ActorsWithFilms): JsValue = Json.obj(
      "id" -> Json.toJson(a.id),
      "firstName" -> Json.toJson(a.firstName),
      "lastName" -> Json.toJson(a.lastName),
      "films" -> Json.toJson(a.films)
    )
  }

}

object ActorsView {
  implicit val actorViewWrites = Json.writes[ActorsView]
}



