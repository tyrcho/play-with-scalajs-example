package example

import scalatags.JsDom.all._
import org.scalajs.dom

import scalajs.js
import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.MouseEvent
import shared.SharedMessages

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

  def cb = input(`type` := "checkbox", id := "cb").render
}
