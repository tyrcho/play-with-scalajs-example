package example

import org.scalajs.dom.ext._
import scala.scalajs.js.JSON
import org.scalajs.dom
import upickle.default._
import org.scalajs.dom
import org.scalajs.dom.MouseEvent
import shared.SharedMessages

import scala.scalajs.js
import scalatags.JsDom.all._
import org.scalajs.dom.raw.Event

object ConfigComponent {
  val ALL_OP = Seq("+", "-", "x", "/")

  val render =
    div(
      ALL_OP.map(configCB),
      style := "font-size:2vw;display:flex").render

  def config = ConfigStore.readConf

  def configCB(label: String) = {
    val i = input(`type` := "checkbox").render
    if (config(label)) i.checked = true
    i.onchange = (e: Event) => (ConfigStore.writeConf(config.updated(label, i.checked)))
    div(i, label).render
  }

  object ConfigStore {
    def readConf: Config =
      LocalStorage("config") match {
        case None       => Map("+" -> true, "-" -> false, "x" -> false, "/" -> false)
        case Some(conf) => read[Config](conf)
      }

    def writeConf(c: Config) =
      LocalStorage.update("config", write(c))

  }

  type Config = Map[String, Boolean]
}