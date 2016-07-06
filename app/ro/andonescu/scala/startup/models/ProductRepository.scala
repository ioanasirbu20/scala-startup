package ro.andonescu.scala.startup.models

import javax.inject.Inject

import com.google.inject.Singleton
import ro.andonescu.scala.startup.models.entity.Product

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/5/2016.
 */
@Singleton
class ProductRepository @Inject() (implicit ec: ExecutionContext) {
  private var products = Set(
    Product(5010255079763L, "Paperclips Large"),
    Product(5018206244666L, "Giant Paperclips")
  )

  def findAll = products.toList.sortBy(_.ean)

  def findAllAsFuture = Future.successful(findAll)

  def findByEan(ean: Long) = products.find(_.ean == ean)
}

