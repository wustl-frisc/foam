package edu.wustl.sbs
package examples

import fsm._
import aspects._

class ChangeReturn extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    //select all the states of type ValueState
    val statePointCut: Pointcut[ValueState] = Pointcutter[State, ValueState](nfa.states, state => state match {
      case s: ValueState => true
      case _ => false
    })

    val transitionPointcut: Pointcut[(ValueState, Token)] = for(s <- statePointCut) yield (s, System("ChangeReturn"))

    Around[(ValueState, Token)](transitionPointcut, nfa)((thisJoinPoint: (ValueState, Token)) => {
        nfa.getTransitions(thisJoinPoint) - nfa.error + ChangeState(thisJoinPoint._1.value)
    })
  }
}
