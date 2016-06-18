package example

import ConfigComponent._

object Judge {

  def build(config: Config) = {
    val operations = candidates(config)
    val op = operations((util.Random.nextFloat() * 1000).toInt % operations.size)
    op(nextInt(), nextInt())
  }

  def candidates(c: Config) = for {
    (op, gen) <- ALL_OP zip ALL_GEN
    if c(op)
  } yield gen

  private def nextInt() =
    (util.Random.nextFloat() * 9 + 2).toInt

  type OperationGenerator = (Int, Int) => Question

  val ADD: OperationGenerator = (a, b) => Question(s"$a + $b", s"${a + b}")
  val MUL: OperationGenerator = (a, b) => Question(s"$a x $b", s"${a * b}")
  val SUB: OperationGenerator = (a, b) => Question(s"${a + b} - $b", s"${a}")
  val DIV: OperationGenerator = (a, b) => Question(s"${a * b} / $b", s"${a}")
  val ALL_GEN = Seq(ADD, SUB, MUL, DIV)
}
