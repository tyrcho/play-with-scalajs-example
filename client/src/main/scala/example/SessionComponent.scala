package example

import org.scalajs.dom.html.Input

import scala.scalajs.js
import scala.scalajs.js.Any._
import org.scalajs.dom.raw.KeyboardEvent

import scalatags.JsDom.all._
import org.scalajs.dom.raw.MouseEvent

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

  val output = div(style := "font-size:2vw").render

  val stopButton: Input = {
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
      div(style := "display:flex", statusDiv, stopButton),
      questionZone,
      div(box),
      output).render

  box.onkeypress = (e: KeyboardEvent) => if (e.keyCode == 13) checkAnswer()

  def checkAnswer() = if (box.value.trim != "") {
    val sign = if (session.ok(box.value)) "✓" else "✘"
    output.insertBefore(div(session.q.toString + s" ($sign)").render, output.firstChild)
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
        div(session.status),
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

    val status = s"$goods ✓, $bads ✘"

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

