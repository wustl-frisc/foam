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

    Following[ValueState](statePointCut, nfa)((thisJoinPoint: ValueState) => (System("ChangeReturn"), ChangeState(thisJoinPoint.value)))
  }
}
