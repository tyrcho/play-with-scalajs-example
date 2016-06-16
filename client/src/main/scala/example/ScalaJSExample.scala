package example

import org.scalajs.dom
import org.scalajs.dom.MouseEvent
import shared.SharedMessages

import scala.scalajs.js
import scalatags.JsDom.all._
import org.scalajs.dom.raw.Event

object ScalaJSExample extends js.JSApp {
  val document = js.Dynamic.global.document
  var question: Option[Question] = None

  def main() = {
    val root = document.getElementById("root")
    root.appendChild(div(
      configZone,
      questionZone,
      div(box),
      output).render)
    showNextQuestion()
  }

  val questionZone = div("question", style := "font-size:10vw").render

  def configCB(label: String) = {
    val i = input(
      `type` := "checkbox",
      name := label,
      id := label,
      value := label,
      checked := config.toMap(label)).render
    i.onchange = (e: Event) => (ConfigStore.writeConf(config.updated(label, i.checked)))
    div(i, label).render
  }

  val configZone = div(
    Seq("+", "-", "x", "/").map(configCB),
    style := "font-size:2vw;display:flex").render

  def config = ConfigStore.readConf

  val box = input(
    `type` := "text",
    autofocus := true,
    style := "font-size:10vw;display:flex",
    placeholder := "?").render

  val output = div(style := "font-size:3vw").render

  box.onkeyup = (e: dom.Event) => checkAnswer()

  def checkAnswer() = {
    if (box.value == question.get.a) {
      output.insertBefore(div(question.get.toString).render, output.firstChild)
      showNextQuestion()
      box.value = ""
      box.focus()
    }
  }

  def showNextQuestion() = {
    question = Some(Judge.build(config))
    questionZone.innerHTML = question.get.q
  }
}

