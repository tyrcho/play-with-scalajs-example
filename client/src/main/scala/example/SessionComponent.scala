package example

import org.scalajs.dom.html.Input
import org.scalajs.dom.html.Button

import Components._

import scala.scalajs.js
import scala.scalajs.js.Any._
import org.scalajs.dom.raw.KeyboardEvent

import scalatags.JsDom.all._
import org.scalajs.dom.raw.MouseEvent
import scala.concurrent.duration.DurationInt

object SessionComponent {
  var session: Session = _

  val questionZone = div(
    "question",
    style := "font-size:6vw").render

  val box = input(
    `type` := "number",
    autofocus := true,
    style := "font-size:6vw; width:30vw",
    placeholder := "?").render

  val output = div(style := "font-size:4vw").render

  val stopButton: Button =
    buttonMaterial("stop", closeSession())

  val statusDiv = div(style := "font-size:3vw").render

  val render =
    div(
      div(style := "display:flex", statusDiv, stopButton),
      questionZone,
      div(box),
      output).render

  box.onkeypress = (e: KeyboardEvent) => if (e.keyCode == 13) checkAnswer()

  def checkAnswer() = if (box.value.trim != "") {
    val ok = session.ok(box.value)
    val sign = if (ok) "✓" else "✘"
    val color = if (ok) "green" else "red"
    val result = div(session.q.toString + s" ($sign)", style := s"color:$color").render
    output.insertBefore(result, output.firstChild)
    scalajs.js.timers.setTimeout(3.seconds)(result.style.fontSize = "2vw")
    session = session.next(box.value)
    updateStatus()
    showNextQuestion()
    box.value = ""
    box.focus()
  }

  def updateStatus() = {
    statusDiv.innerHTML = ""
    statusDiv.appendChild(
      div(
        div(
          span(color := "green", session.goods + "✓"),
          span(", "),
          span(color := "red", session.bads + "✘")),
        div(
          style := "background-color: #ddd; height: 2vw; width: 10vw",
          div(
            style := s"background-color: #4CAF50; height: 100%;width:${session.progress}%"))).render)
  }

  def startSession() = {
    session = Session(Vector.fill(10)(Judge.build()).distinct)
    output.innerHTML = ""
    setEnabled(true)
    showNextQuestion()
    updateStatus()
  }

  def showNextQuestion() = {
    if (session.hasNext) {
      questionZone.innerHTML = session.q.q
      box.focus()
    } else closeSession()
  }

  private def setEnabled(en: Boolean) = {
    val display = if (en) "" else "none"
    Seq(stopButton, box, questionZone).foreach(_.style.display = display)
  }

  private def closeSession() = {
    setEnabled(false)
    ended()
  }

  var ended: (() => Unit) = () => ()

  case class Session(questions: Vector[Question], pos: Int = 0, goods: Int = 0, bads: Int = 0) {
    val size = questions.size
    val todo = size - pos
    val progress = ((size - todo) * 100 / size).toInt // percentage completed

    def q = questions(pos)

    def ok(ans: String) = q.check(ans)

    def hasNext = pos < questions.size

    def next(ans: String) = copy(
      pos = pos + 1,
      goods = goods + (if (ok(ans)) 1 else 0),
      bads = bads + (if (ok(ans)) 0 else 1))

    def ratio = goods.toFloat / size
  }

}

