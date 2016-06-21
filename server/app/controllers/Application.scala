package controllers

import java.text.SimpleDateFormat
import java.util.Date

import play.api.Environment
import play.api.mvc._
import shared.SharedMessages

class Application()(implicit environment: Environment) extends Controller {

  def index = Action {
    Ok(views.html.index(SharedMessages.itWorks))
  }

  val cacheManifest = {
    val date=new SimpleDateFormat("dd MMMM yyyy hh:mm:ss").format(new Date)
    Action { implicit request =>
      Ok(
        s"""CACHE MANIFEST
            |# $date
            |${routes.Assets.at("stylesheets/main.css").url}
            |${routes.Assets.at("lib/jquery/jquery.min.js").url}
            |/assets/client-jsdeps.js
            |/assets/client-fastopt.js
            |/assets/client-opt.js
            |/assets/client-launcher.js
         """.stripMargin).as("text/cache-manifest")
    }
  }

}
