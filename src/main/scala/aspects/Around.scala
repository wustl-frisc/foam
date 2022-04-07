package edu.wustl.sbs
package aspects

import fsm._

object Around {
  def apply[A <: TransitionKey](pointcut: Pointcut[A], base: NFA)(body: A => State) = {
    Advice[A, NFA](pointcut, base)((prevBase, jp) => {
      prevBase.addTransition(jp, body(jp))
    })
  }
}
