
package foam
package aspects

object Pointcut {
  def apply[A](): Pointcut[A] = Set[A]()
}
