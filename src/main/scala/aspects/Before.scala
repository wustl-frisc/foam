package edu.wustl.sbs
package aspects

import fsm._

object Before {
  def apply[A <: State](pointcut: Pointcut[A], base: NFA)(body: A => (State, Token)) = {
    Advice[A, NFA](pointcut, base)((prevBase, jp) => {
      prevBase.addTransition((body(jp)._1, body(jp)._2), jp)
    })
  }
}
