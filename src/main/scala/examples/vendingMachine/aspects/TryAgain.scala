package edu.wustl.sbs
package examples

import fsm._
import aspects._

class TryAgain extends Aspect[NFA] {
  def apply(nfa: NFA) = {
    val transitionKeyPointcut = Pointcutter[TransitionKey, TransitionKey](nfa.transitions.keys, k => k._1 match {
      case s: ValueState => k._2 match { //the state in the key is a value state
        case t: Product if (s.value - t.value < 0) => true //the token in the key is a product
        case _ => false
      }
      case _ => false
    })

    Around[TransitionKey](transitionKeyPointcut, nfa)((thisJoinPoint: TransitionKey) => nfa.transitions(thisJoinPoint) + thisJoinPoint._1)
  }
}
