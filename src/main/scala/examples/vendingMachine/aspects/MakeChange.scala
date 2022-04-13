package edu.wustl.sbs
package examples

import fsm._
import aspects._

class MakeChange extends Aspect[NFA] {
  def apply(nfa: NFA) = {
    val transitionKeyPointcut = Pointcutter[TransitionKey, (ValueState, Product)](nfa.transitions.keys, k => k._1 match {
      case s: ValueState => k._2 match { //the state in the key is a value state
        case t: Product if (s.value - t.value >= 0) => true //the token in the key is a product
        case _ => false
      }
      case _ => false
    })

    val nfaWithChange = Around[(ValueState, Product)](transitionKeyPointcut, nfa)((thisJoinPoint: (ValueState, Product)) => ChangeState(thisJoinPoint._1.value - thisJoinPoint._2.value))

    val statePointCut: Pointcut[ChangeState] = Pointcutter[State, ChangeState](nfa.states, state => state match {
      case s: ChangeState => true
      case _ => false
    })

    Following[ChangeState](statePointCut, nfaWithChange)((s: ChangeState) => (Lambda, nfaWithChange.acceptState))
  }
}
