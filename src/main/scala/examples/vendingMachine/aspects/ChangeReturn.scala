package edu.wustl.sbs
package examples

import fsm._
import aspects._

class ChangeReturn extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    //select all the states of type ValueState
    val statePointCut: Pointcut[ValueState] = Pointcutter[State](nfa.states, state => state match {
      case s: ValueState => true
      case _ => false
    }) map {_.asInstanceOf[ValueState]}

    Advice[ValueState, NFA](statePointCut, nfa)((prevNFA, state) => prevNFA.addTransition((state, System("ChangeReturn")), ChangeState(state.value)))
  }
}
