package example

import org.scalajs.dom
import org.scalajs.dom.MouseEvent
import shared.SharedMessages

import scala.scalajs.js
import scalatags.JsDom.all._

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

  def configCB(label: String) =
    div(
      input(
        `type` := "checkbox",
        name := label,
        id := label,
        value := label,
        checked := true),
      label).render

  val configZone = div(
    Seq("+", "-", "x", "/").map(configCB),
    style := "font-size:2vw;display:flex").render

  def config = {
    val Seq(add, sub, mul, div) = Seq("+", "-", "x", "/").map(op => document.getElementById(op).checked.asInstanceOf[Boolean])
    Config(add, sub, mul, div)
  }

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

object Judge {
  def build(config: Config) = config.next(util.Random.nextFloat())(nextInt(), nextInt())

  private def nextInt() =
    (util.Random.nextFloat() * 9 + 2).toInt

  type OperationGenerator = (Int, Int) => Question

  val ADD: OperationGenerator = (a, b) => Question(s"$a + $b", s"${a + b}")
  val MUL: OperationGenerator = (a, b) => Question(s"$a x $b", s"${a * b}")
  val SUB: OperationGenerator = (a, b) => Question(s"${a + b} - $b", s"${a}")
  val DIV: OperationGenerator = (a, b) => Question(s"${a * b} / $b", s"${a}")
}

case class Question(q: String, a: String, start: Long = System.nanoTime) {
  override def toString = s"$q = $a ($durationMs)"
  def durationMs = (System.nanoTime - start) / 1000000
}

case class Config(add: Boolean, sub: Boolean, mul: Boolean, div: Boolean) {
  import Judge._

  def next(rng: Float): OperationGenerator = {
    candidates((rng * 1000).toInt % candidates.size)
  }

  val candidates = for {
    (b, op) <- Seq(add, sub, mul, div) zip Seq(ADD, SUB, MUL, DIV)
    if b
  } yield op

}
