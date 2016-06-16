package example

import org.scalajs.dom
import org.scalajs.dom.MouseEvent
import shared.SharedMessages

import scala.scalajs.js
import scalatags.JsDom.all._

object ScalaJSExample extends js.JSApp {
  val doc = dom.document

  def main() = {
    doc.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
    val root = doc.getElementById("root")
    root.appendChild(div("test").render).appendChild(cb)
    root.appendChild (p("hello").render)
    cb.onclick = {
      e: MouseEvent => root.appendChild (p("hello").render)
    }
  }

  val cb = input(`type` := "checkbox", id := "cb").render
}
