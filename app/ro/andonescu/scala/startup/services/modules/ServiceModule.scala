package ro.andonescu.scala.startup.services.modules

import play.api.inject.Module
import play.api.{Configuration, Environment}
import ro.andonescu.scala.startup.services.{ActorsService, ActorsServiceImpl}

/**
 * Created by V3790155 on 7/26/2016.
 */
class ServiceModule extends Module {

  def bindings(environment: Environment, configuration: Configuration) = Seq(
    bind[ActorsService].to[ActorsServiceImpl]
  )
}
