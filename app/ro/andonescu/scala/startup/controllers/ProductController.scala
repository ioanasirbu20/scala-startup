package ro.andonescu.scala.startup.controllers

import javax.inject.Inject

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import ro.andonescu.scala.startup.models.ProductRepository
import ro.andonescu.scala.startup.models.entity.Product

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by V3790155 on 7/5/2016.
 */
class ProductController @Inject() (repo: ProductRepository, implicit val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {
  def list = Action {
    val products = repo.findAll
    Ok(views.html.list(products, productForm))
  }

  def list2 = Action.async {
    repo.findAllAsFuture.map { product =>
      Ok(views.html.list(product, productForm))
    }
  }

  def show(ean: Long) = Action {
    val product = repo.findByEan(ean)
    Ok(views.html.details(product))
  }

  private val productForm: Form[Product] = Form(
    mapping(
      "ean" -> longNumber.verifying(
        "validation.ean.duplicate", repo.findByEan(_).isEmpty
      ),
      "name" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )

  def addProduct = Action.async { implicit request =>
    productForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.list(repo.products.toList, errorForm)))
      },
      product => {

        repo.products = repo.products :+ product
        Future.successful(Ok(views.html.list(repo.products.toList, productForm)))
      }
    )
  }

}
