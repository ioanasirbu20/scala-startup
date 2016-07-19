package ro.andonescu.scala.startup.models.entity

import play.api.libs.json.{Json, __}

/**
 * Created by V3790155 on 7/5/2016.
 */
case class Product(id: Long, ean: Long, name: String)

object Product {

  implicit val productFormat = Json.format[Product]
}