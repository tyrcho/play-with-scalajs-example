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
}

//object QuestionStore {
//
//  type Questions = Map[String, Question]
//
//  def questions = readQuestions
//
//  def update(q: Question) =
//    writeQuestions(readQuestions + (q.q -> q))
//
//  def readQuestions: Questions =
//    LocalStorage("questions") match {
//      case None    => Map.empty
//      case Some(q) => read[Questions](q)
//    }
//
//  def writeQuestions(q: Questions) =
//    LocalStorage.update("questions", write(q))
//}
