package ro.andonescu.scala.startup.models.entity

import org.joda.time.DateTime
import play.api.libs.json.Json

case class FilmActor(actorId: Long, filmId: Long)

object FilmActor {
  implicit val filmActorFormat = Json.format[FilmActor]
}