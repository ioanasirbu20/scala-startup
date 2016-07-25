package ro.andonescu.scala.startup.controllers.jsons

import org.joda.time.DateTime
import play.api.libs.json._
import ro.andonescu.scala.startup.models.entity.Actor
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

/**
 * Created by V3790155 on 7/22/2016.
 */
case class ActorForm(firstName: String, lastName: String)

case class ActorsView(items: Seq[Actor])

object ActorsView {

  implicit object ActorWrites extends Writes[Actor] {
    override def writes(a: Actor): JsValue = Json.obj(
      "id" -> Json.toJson(a.id),
      "firstName" -> Json.toJson(a.firstName),
      "lastName" -> Json.toJson(a.lastName)
    )
  }

  implicit val actorViewFormat = Json.writes[ActorsView]
}

object ActorForm {
  implicit val actorFormReads: Reads[ActorForm] = (
    (JsPath \ "firstName").read[String](minLength[String](2)) and
    (JsPath \ "lastName").read[String](minLength[String](5))
  )(ActorForm.apply _)
}