package ro.andonescu.scala.startup.models.entity

import org.joda.time.DateTime
import play.api.libs.json.{Json, _}

/**
 * Created by V3790155 on 7/21/2016.
 */
case class Language(id: Long, name: String, lastUpdate: DateTime)

object Language {
  implicit val languageFormat = Json.format[Language]
}
