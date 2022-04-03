package edu.wustl.sbs
package aspects

object Advice {
  def apply[A, B](pointcut: Pointcut[A], base: B, transform: A => B) = {
    pointcut.foldLeft(base)((newBase, joinpoint) => transform(joinpoint))
  }
}
