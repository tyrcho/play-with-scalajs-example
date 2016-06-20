package example

import scala.scalajs.js
import scala.scalajs.js.Any._

import org.scalajs.dom.raw.KeyboardEvent

import scalatags.JsDom.all._
import org.scalajs.dom.raw.MouseEvent

object SessionComponent {
  var session: Session = _


  val questionZone = div(
    "question",
    style := "font-size:10vw").render

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
    output.insertBefore(div(session.q.toString).render, output.firstChild)
    session = session.next(box.value)
    showNextQuestion()
    box.value = ""
    box.focus()
  }

  def startSession() = {
    session = Session(Vector.fill(10)(Judge.build()))
    output.innerHTML = ""
    showNextQuestion()
  }

  def showNextQuestion() = {
    if (session.hasNext) {
      questionZone.innerHTML = session.q.q
      box.focus()
    } else closeSession()
  }

  private def closeSession() = {
    ended()
  }

  var ended: (() => Unit) = () => ()

  case class Session(questions: Vector[Question], pos: Int = 0, goods: Int = 0, bads: Int = 0) {
    def todo = questions.size - pos

    def q = questions(pos)

    def ok(ans: String) = q.a == ans

    def hasNext = pos < questions.size

    def next(ans: String) = copy(
      pos = pos + 1,
      goods = goods + (if (ok(ans)) 1 else 0),
      bads = bads + (if (ok(ans)) 0 else 1)
    )
  }

}

