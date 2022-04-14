package edu.wustl.sbs
package aspects

import fsm._

object After {
  def apply[A <: State](pointcut: Pointcut[A], base: NFA)(body: A => (Token, State)) = {
    Advice[A, NFA](pointcut, base)((prevBase, jp) => {
      prevBase.addTransition((jp, body(jp)._1), body(jp)._2)
    })
  }
}
