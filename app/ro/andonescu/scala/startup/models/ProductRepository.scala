package ro.andonescu.scala.startup.models

import javax.inject.Inject

import com.google.inject.Singleton
import ro.andonescu.scala.startup.models.entity.Product
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/5/2016.
 */
@Singleton
class ProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class ProductTable(tag: Tag) extends Table[Product](tag, "product") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def ean = column[Long]("ean")

    def name = column[String]("name")

    def * = (id, ean, name) <> ((Product.apply _).tupled, Product.unapply)
  }

  private val product = TableQuery[ProductTable]

  def create(ean: Long, name: String): Future[Product] = db.run {
    (product.map(p => (p.ean, p.name))
      returning product.map(_.id)
      into ((eanName, id) => Product(id, eanName._1, eanName._2))) += (ean, name)
  }

  def findAll(): Future[Seq[Product]] = db.run {
    product.result
  }

  //  def findAll = products.toList.sortBy(_.ean)

  //  def findAllAsFuture = Future.successful(findAll)

  def findByEan(ean: Long): Future[Option[Product]] = db.run {
    product.filter(f => f.ean === ean).result.headOption
  }
}

