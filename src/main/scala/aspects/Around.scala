package edu.wustl.sbs
package aspects

import fsm._

object Around {
  def apply[A <: TransitionKey](pointcut: Pointcut[A], base: NFA)(body: A => Set[State]) = {
    Advice[A, NFA](pointcut, base)((prevBase, jp) => {
      body(jp).foldLeft(prevBase.clearTransitions(jp))((modBase, state) => {
        modBase.addTransition(jp, state)
      })
    })
  }
}
