package ro.andonescu.scala.startup.controllers

import javax.inject.Inject

import play.api.mvc.{Action, Controller}
import ro.andonescu.scala.startup.models.ProductRepository

import scala.concurrent.ExecutionContext

/**
 * Created by V3790155 on 7/5/2016.
 */
class ProductController @Inject() (repo: ProductRepository)(implicit ec: ExecutionContext) extends Controller {
  def list = Action {
    val products = repo.findAll
    Ok(views.html.list(products))
  }

  def list2 = Action.async {
    repo.findAllAsFuture.map { product =>
      Ok(views.html.list(product))
    }
  }

}
