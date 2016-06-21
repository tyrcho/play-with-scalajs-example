package example

import org.scalajs.dom.ext.LocalStorage

import upickle.default._

case class Question(q: String, a: String, goods: Int = 0, bads: Int = 0) {
  override def toString = s"$q = $a"

  val total = goods + bads

  //between -1 (all wrong) and 1 (all good). 0 = never done
  val score =
    if (total == 0) 0
    else if (goods == 0) -1
    else goods.toFloat / total

  def check(ans: String) = ans.trim.dropWhile(_ == '0') == a.trim
}