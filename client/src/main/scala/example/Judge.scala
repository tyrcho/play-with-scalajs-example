package example

import ConfigComponent._
import scala.util.Random

object Judge {

  def build() = {
    val qs = for {
      (op, gen) <- ALL_OP zip ALL_GEN
      if config(op)
      l = level(op)
      a = nextInt(l, l * 3F / 5)
      b = nextInt(l, 1)
    } yield gen(a, b)
    Random.shuffle(qs).head
  }

  private def nextInt(max: Int, min: Float) =
    (util.Random.nextFloat() * (max + 1 - min) + min).toInt

  type OperationGenerator = (Int, Int) => Question

  val ADD: OperationGenerator = (a, b) => Question(s"$a + $b", s"${a + b}")
  val MUL: OperationGenerator = (a, b) => Question(s"$a x $b", s"${a * b}")
  val SUB: OperationGenerator = (a, b) => Question(s"${a + b} - $b", s"${a}")
  val DIV: OperationGenerator = (a, b) => Question(s"${a * b} / $b", s"${a}")
  val ALL_GEN = Seq(ADD, SUB, MUL, DIV)
}
