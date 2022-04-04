package edu.wustl.sbs
package aspects

import fsm._

object Advice {
  def apply[A, B](pointcut: Pointcut[A], base: B)(transform: (B, A) => B) = {
    pointcut.foldLeft(base)((newBase, joinpoint) => transform(newBase, joinpoint))
  }
}
