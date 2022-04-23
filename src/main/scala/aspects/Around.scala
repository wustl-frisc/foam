package edu.wustl.sbs
package aspects

import fsm._

object Around {
  def apply[A <: State](pointcut: Pointcut[A], base: NFA)(body: (A, NFA) => (A, NFA)) = {
    Advice[A, NFA](pointcut, base)((prevBase, jp) => {
      val (advice, newNFA) = body(jp, prevBase)

      val oldIns = newNFA.getIns(jp)
      val oldOuts = newNFA.getOuts(jp)

      val step1 = oldIns.foldLeft(newNFA)((prevNFA, key) => {
        prevNFA.removeTransition(key, jp).addTransition(key, advice)
      })

      oldOuts.foldLeft(step1)((prevNFA, key) => {
        val destinations = prevNFA.transitions(key)
        destinations.foldLeft(prevNFA)((adviceNFA, state) => {
          adviceNFA.clearTransitions(key).addTransition((advice, key._2), state)
        })
      })
    })
  }
}
