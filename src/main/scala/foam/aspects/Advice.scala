
package foam
package aspects

object Advice {
  def apply[A, B](pointcut: Pointcut[A], base: B)(transform: (B, A) => B): B = {
    pointcut.foldLeft(base)((newBase, joinpoint) => transform(newBase, joinpoint))
  }
}
