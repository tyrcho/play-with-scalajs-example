package example

case class Question(q: String, a: String, start: Long = System.nanoTime) {
  override def toString = s"$q = $a ($durationMs)"
  def durationMs = (System.nanoTime - start) / 1000000
}
