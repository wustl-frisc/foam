package edu.wustl.sbs
package aspects

import fsm._

object After {
  def apply[A <: State](pointcut: Pointcut[A], base: NFA)(body: A => (Token, State)) = {
    Advice[A, NFA](pointcut, base)((prevBase, jp) => {
      val newKey = (jp, body(jp)._1)

      val alphaPointcut = Pointcutter[TransitionKey, TransitionKey](prevBase.transitions.keys,
        key => key match {
          case k: TransitionKey if(k._1 == jp && k._2 != Lambda) => true
          case _ => false
      })

      val deltaPointcut: Pointcut[TransitionKey] = for((_, t) <- alphaPointcut) yield (body(jp)._2, t)

      val step1 = Around[TransitionKey](deltaPointcut, prevBase)((thisJoinPoint: TransitionKey) => {
        prevBase.getTransitions((jp, thisJoinPoint._2))
      })

      val step2 = Around[TransitionKey](alphaPointcut, step1)((thisJoinPoint: TransitionKey) => Set[State](base.error))

      step2.clearTransitions(newKey).addTransitions(newKey, step2.getTransitions(newKey) - base.error + body(jp)._2)
    })
  }
}
