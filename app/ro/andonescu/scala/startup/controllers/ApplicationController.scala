package ro.andonescu.scala.startup.controllers

import java.lang.ProcessBuilder.Redirect
import javax.inject.Inject

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

import scala.concurrent.ExecutionContext

/**
 * Created by V3790155 on 7/11/2016.
 */
class ApplicationController @Inject() (val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {
  def homepage = Action {
    Ok(views.html.homepage())
  }

  def persons = Action {
    Ok(views.html.persons())
  }

  def products = Action {
    Ok(views.html.products())
  }
}
