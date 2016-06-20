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
      style := "font-size:3vw").render
    i.onclick = (e: MouseEvent) => closeSession()
    i
  }

  val statusDiv = div(style := "font-size:3vw").render

  val render =
    div(
      questionZone,
      div(style := "display:flex", statusDiv, stopButton),
      div(box),
      output).render

  box.onkeypress = (e: KeyboardEvent) => if (e.keyCode == 13) checkAnswer()

  def checkAnswer() = {
    val sign = if (session.ok(box.value)) "✓" else "✘"
    output.insertBefore(div(session.q.toString + s" ($sign)").render, output.firstChild)
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
      statusDiv.textContent = session.status
      questionZone.innerHTML = session.q.q
      box.focus()
    } else closeSession()
  }

  private def closeSession() = {
    ended()
  }

  var ended: (() => Unit) = () => ()

  case class Session(questions: Vector[Question], pos: Int = 0, goods: Int = 0, bads: Int = 0) {
    val size = questions.size
    val todo = size - pos

    val status = s"$goods ✓, $bads ✘, $todo ? ($size)"

    def q = questions(pos)

    def ok(ans: String) = q.check(ans)

    def hasNext = pos < questions.size

    def next(ans: String) = copy(
      pos = pos + 1,
      goods = goods + (if (ok(ans)) 1 else 0),
      bads = bads + (if (ok(ans)) 0 else 1)
    )
  }

}

