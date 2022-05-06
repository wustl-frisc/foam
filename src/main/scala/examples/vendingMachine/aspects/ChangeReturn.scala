package edu.wustl.sbs
package examples

import fsm._
import aspects._

class ChangeReturn extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    //select all the states of type TotalState
    val statePointCut = Pointcutter[State, TotalState](nfa.states, state => state match {
      case s: TotalState => true
      case _ => false
    })

    Around[TotalState](statePointCut, nfa)((thisJoinPoint: Joinpoint[TotalState], thisNFA: NFA) => {
      val newNFA = thisNFA.addTransition((thisJoinPoint.point, System("ChangeReturn")),
        ChangeState(thisJoinPoint.point.value, true))
      (thisJoinPoint.point, newNFA)
    })
  }
}
