package edu.wustl.sbs
package aspects

import fsm._

object Before {
  def apply[A <: State](pointcut: Pointcut[A], base: NFA)(body: A => (State, Token)) = {
    Advice[A, NFA](pointcut, base)((prevBase, jp) => {
      val newKey = body(jp)

      val transitionPointcut = Pointcutter[TransitionKey, TransitionKey](prevBase.transitions.keys,
        key => key match {
          case k: TransitionKey if((prevBase.getTransitions(k) contains jp) &&
            k != newKey) => true
          case _ => false
      })

      val step1 = Around[TransitionKey](transitionPointcut, prevBase)((thisJoinPoint: TransitionKey) => {
        prevBase.getTransitions(thisJoinPoint) - base.error - jp + newKey._1
      })

      step1.clearTransitions(newKey).addTransitions(newKey, step1.getTransitions(newKey) - base.error + jp)
    })
  }
}
