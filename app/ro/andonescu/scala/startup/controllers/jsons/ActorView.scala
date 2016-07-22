package ro.andonescu.scala.startup.controllers.jsons

import play.api.libs.json.{JsValue, Writes, Json}
import ro.andonescu.scala.startup.models.entity.Actor

/**
 * Created by V3790155 on 7/22/2016.
 */
case class ActorView(items: Seq[Actor])

object ActorView {
  implicit object ActorWrites extends Writes[Actor] {
    override def writes(a: Actor): JsValue = Json.obj(
      "id" -> Json.toJson(a.id),
      "firstName" -> Json.toJson(a.firstName),
      "lastName" -> Json.toJson(a.lastName)
    )
  }

  implicit val actorViewFormat = Json.writes[ActorView]
}
