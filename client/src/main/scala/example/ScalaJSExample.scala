package example

import scala.scalajs.js
import scala.scalajs.js.Any._

import org.scalajs.dom.raw.KeyboardEvent

import scalatags.JsDom.all._
import org.scalajs.dom.raw.MouseEvent

object ScalaJSExample extends js.JSApp {
  val document = js.Dynamic.global.document
  val root = document.getElementById("root")

  SessionComponent.ended = () => {
    ConfigComponent.update(SessionComponent.session.ratio >= 0.9)
    startButton.disabled = false
    //root.removeChild(SessionComponent.render)
    startButton.focus()
  }

  def main() = {
    root.appendChild(
      div(
        style := "display:flex",
        ConfigComponent.render,
        startButton)
        .render)
    startButton.focus()
  }

  val startButton = {
    val i = input(
      `type` := "button",
      value := "Start",
      style := "font-size:4vw").render
    i.onclick = (e: MouseEvent) => startSession()
    i
  }

  def startSession() {
    startButton.disabled = true
    root.appendChild(SessionComponent.render)
    SessionComponent.startSession()
  }

}

