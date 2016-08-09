package ro.andonescu.scala.startup.models.entity

import org.joda.time.DateTime
import play.api.libs.json.Json

/**
 * Created by V3790155 on 8/9/2016.
 */
case class FilmCategory(filmId: Long, categoryId: Long, lastUpdate: DateTime)

object FilmCategory {
  implicit val filmCategory = Json.format[FilmCategory]
}
