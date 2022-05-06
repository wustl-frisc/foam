package edu.wustl.sbs
package examples

import fsm._
import aspects._

class InsufficientFunds extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    //select all the states of type TotalState
    val statePointCut = Pointcutter[State, TotalState](nfa.states, state => state match {
      case s: TotalState => true
      case _ => false
    })

    BeforeState[TotalState](statePointCut, nfa)((thisJoinPoint: StateJoinpoint[TotalState], thisNFA: NFA) => {
      thisJoinPoint.in.get._2 match {
        case t: Product => (Some((PrinterState("Insufficient Funds", thisJoinPoint.point.value, false), Lambda)), thisNFA)
        case _ => (None, thisNFA)
      }
    })
  }
}
