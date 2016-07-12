package ro.andonescu.scala.startup.models.entity

import play.mvc.BodyParser.Json

/**
 * Created by V3790155 on 7/5/2016.
 */
case class Product(id: Long, ean: Long, name: String)

