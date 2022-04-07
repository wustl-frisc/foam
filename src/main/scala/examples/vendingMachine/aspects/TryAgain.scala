package edu.wustl.sbs
package examples

import fsm._
import aspects._

class TryAgain extends Aspect[NFA] {
  def apply(nfa: NFA) = {
    val transitionKeyPointcut = Pointcutter[TransitionKey](nfa.transitions.keys, k => k._1 match {
      case s: ValueState => k._2 match { //the state in the key is a value state
        case t: Product if (k._1.asInstanceOf[ValueState].value - k._2.asInstanceOf[Product].value < 0) => true //the token in the key is a product
        case _ => false
      }
      case _ => false
    })

    Around[TransitionKey](transitionKeyPointcut, nfa)((thisJoinPoint: TransitionKey) => thisJoinPoint._1)
  }
}
