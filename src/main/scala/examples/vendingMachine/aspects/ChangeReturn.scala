package edu.wustl.sbs
package examples

import fsm._
import aspects._

class ChangeReturn extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    //select all the states of type ValueState
    val statePointCut = Pointcutter[State, ValueState](nfa.states, state => state match {
      case s: ValueState => true
      case _ => false
    })

    Around[ValueState](statePointCut, nfa)((thisJoinPoint: Joinpoint[ValueState], thisNFA: NFA) => {
      val newNFA = thisNFA.addTransition((thisJoinPoint.point, System("ChangeReturn")),
        ChangeState(thisJoinPoint.point.value, true))
      (thisJoinPoint.point, newNFA)
    })
  }
}
