package ro.andonescu.scala.startup.models.entity

import org.joda.time.DateTime
import play.api.libs.json.Json

/**
 * Created by V3790155 on 8/1/2016.
 */
case class FilmActor(actorId: Long, filmId: Long)

object FilmActor {
  implicit val filmActorFormat = Json.format[FilmActor]
}