package ro.andonescu.scala.startup.controllers.jsons

import play.api.libs.json.Json
import ro.andonescu.scala.startup.models.entity.Film

/**
 * Created by V3790155 on 7/25/2016.
 */
case class FilmsView(items: Seq[Film])

object FilmsView {
  implicit val filmViewFormat = Json.format[FilmsView]
}
