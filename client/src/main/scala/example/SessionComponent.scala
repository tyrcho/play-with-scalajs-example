package example

import scala.scalajs.js
import scala.scalajs.js.Any._

import org.scalajs.dom.raw.KeyboardEvent

import scalatags.JsDom.all._
import org.scalajs.dom.raw.MouseEvent

object SessionComponent {
  var questionPos = 0
  val questions = Seq.fill(10)(Judge.build())

  def q = questions(questionPos)

  val questionZone = div("question", style := "font-size:10vw").render

  val box = input(
    `type` := "number",
    autofocus := true,
    style := "font-size:10vw",
    placeholder := "?").render

  val output = div(style := "font-size:3vw").render

  val stopButton = {
    val i = input(
      `type` := "button",
      value := "Stop",
      style := "font-size:4vw").render
    i.onclick = (e: MouseEvent) => closeSession()
    i
  }

  val render = {
    div(
      questionZone,
      stopButton,
      div(box),
      output).render
  }

  box.onkeypress = (e: KeyboardEvent) => if (e.keyCode == 13) checkAnswer()

  def checkAnswer() = {
    output.insertBefore(div(q.toString).render, output.firstChild)
    showNextQuestion()
    box.value = ""
    box.focus()
  }

  def startSession() = {
    output.innerHTML = ""
    questionPos = 0
    showNextQuestion()
  }

  def showNextQuestion() = {
    questionPos += 1
    if (questionPos < questions.size)
      questionZone.innerHTML = q.q
    else closeSession()
  }

  private def closeSession() = {
    ended()
  }

  var ended: (() => Unit) = () => ()
}

