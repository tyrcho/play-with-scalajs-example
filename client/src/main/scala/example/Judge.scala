package example

import ConfigComponent._
import scala.util.Random

object Judge {

  //  def build() = {
  //    val all = if (QuestionStore.readQuestions.isEmpty) {
  //      val qs = for {
  //        (op, gen) <- ALL_OP zip ALL_GEN
  //        if config(op)
  //        l = level(op)
  //        a <- 1 to l
  //        b <- 1 to l
  //      } yield gen(a, b)
  //      QuestionStore.writeQuestions(qs.map(q => q.q -> q).toMap)
  //      qs
  //    } else QuestionStore.readQuestions.values
  //
  //    all.minBy(_.score)
  //  }

  def build() = {
    val qs = for {
      (op, gen) <- ALL_OP zip ALL_GEN
      if config(op)
      l = level(op)
      a = nextInt(l)
      b = nextInt(l)
    } yield gen(a, b)
    Random.shuffle(qs).head
  }

  private def nextInt(max: Int) =
    (util.Random.nextFloat() * max + 1).toInt

  type OperationGenerator = (Int, Int) => Question

  val ADD: OperationGenerator = (a, b) => Question(s"$a + $b", s"${a + b}")
  val MUL: OperationGenerator = (a, b) => Question(s"$a x $b", s"${a * b}")
  val SUB: OperationGenerator = (a, b) => Question(s"${a + b} - $b", s"${a}")
  val DIV: OperationGenerator = (a, b) => Question(s"${a * b} / $b", s"${a}")
  val ALL_GEN = Seq(ADD, SUB, MUL, DIV)
}
