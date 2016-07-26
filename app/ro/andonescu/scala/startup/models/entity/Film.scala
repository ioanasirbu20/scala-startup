package ro.andonescu.scala.startup.models.entity

import play.api.libs.json.Json

/**
 * Created by V3790155 on 7/25/2016.
 */
case class Film(id: Long, title: String, description: String, releaseYear: Int,
  languageId: Int, originalLanguageId: Int, rentalDuration: Int,
  rentalRate: Float, length: Int, replacementCost: Float, rating: String)
