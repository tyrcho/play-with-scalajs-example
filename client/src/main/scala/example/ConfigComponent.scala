package example

import scala.scalajs.js
import scala.scalajs.js.Any._

import org.scalajs.dom.ext.LocalStorage
import org.scalajs.dom.raw.Event

import scalatags.JsDom.all._
import upickle.default._

object ConfigComponent {
  val ALL_OP = Seq("+", "-", "x", "/")

  val render =
    div(
      ALL_OP.map(configCB),
      style := "font-size:2vw").render

  def config = ConfigStore.readConf

  def level = ConfigStore.readLevel

  def update(success: Boolean) = {
    for {
      (label, sel) <- config
      if sel
      lvlInput = js.Dynamic.global.document.getElementById(label + "level")
    } {
      if (success) lvlInput.stepUp()
      else lvlInput.stepDown()
      ConfigStore.writeLevel(ConfigStore.readLevel.updated(label, lvlInput.value.asInstanceOf[String].toInt))
    }
  }

  def configCB(label: String) = {
    val i = input(`type` := "checkbox").render
    if (config(label)) i.checked = true
    i.onchange = (e: Event) => ConfigStore.writeConf(config.updated(label, i.checked))
    val level = input(
      id := label + "level",
      value := ConfigStore.readLevel(label),
      style := "width:3vw",
      `type` := "number",
      min := "1",
      max := "10").render
    level.onchange = (e: Event) => ConfigStore.writeLevel(ConfigStore.readLevel.updated(label, level.value.toInt))
    div(i, label, level).render
  }

  object ConfigStore {
    def readConf: Config =
      LocalStorage("config") match {
        case None => Map("+" -> true, "-" -> false, "x" -> false, "/" -> false)
        case Some(conf) => read[Config](conf)
      }

    def writeConf(c: Config) =
      LocalStorage.update("config", write(c))

    def readLevel: Level =
      LocalStorage("levels") match {
        case None => ALL_OP.map(o => o -> 2).toMap
        case Some(l) => read[Level](l)
      }

    def writeLevel(c: Level) =
      LocalStorage.update("levels", write(c))

  }

  type Config = Map[String, Boolean]
  type Level = Map[String, Int]
}