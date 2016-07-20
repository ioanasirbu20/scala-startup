package ro.andonescu.scala.startup.controllers

import javax.inject.Inject

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import ro.andonescu.scala.startup.models.ProductRepository
import ro.andonescu.scala.startup.models.entity.Product

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by V3790155 on 7/5/2016.
  */
class ProductController @Inject()(repo: ProductRepository, implicit val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {
  def getProducts = Action.async {
    repo.findAll().map { products =>
      Ok(Json.toJson(products))
    }
  }

  def index = Action {
    Ok(views.html.index())
  }

  //  def list2 = Action.async {
  //    repo.findAllAsFuture.map { product =>
  //      Ok(views.html.getProducts(product))
  //    }
  //  }

  def addAProduct = Action {
    Ok(views.html.addAProduct(productForm))
  }

  def show(ean: Long) = Action.async {
    repo.findByEan(ean).map { product =>
      Ok(Json.toJson(product))
    }
  }

  def save(ean: Long) = Action.async(parse.json) { request =>

    val productJson = request.body
    val product = productJson.as[PutProductForm](PutProductForm.putProductFormat)

    repo.saveByEan(product.ean, product.name).map {
      case 0 => BadRequest("Product not found")
      case _ => Ok("saved")
    }

  }

  private val productForm: Form[CreateProductForm] = Form(
    mapping(
      "ean" -> longNumber, //longNumber.verifying("validation.ean.duplicate", repo.findByEan(_).isEmpty)
      "name" -> nonEmptyText
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  )

  def addProduct = Action.async { implicit request =>
    productForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.addAProduct(errorForm)))
      },
      product => {

        repo.create(product.ean, product.name).map { _ =>
          (Redirect(routes.ProductController.getProducts()))
        }
      }
    )
  }
}

case class CreateProductForm(ean: Long, name: String)

case class PutProductForm(ean: Long, name: String)

object PutProductForm {

  implicit def putProductFormat = Json.format[PutProductForm]
}