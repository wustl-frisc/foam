package edu.wustl.sbs
package examples

import fsm._
import aspects._

class PrintFunds extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    //select all the states of type TotalState
    val statePointCut = Pointcutter[State, TotalState](nfa.states, state => state match {
      case s: TotalState => true
      case _ => false
    })

    Before[TotalState](statePointCut, nfa)((thisJoinPoint: Joinpoint[TotalState], thisNFA: NFA) => {
      thisJoinPoint.in.get._2 match {
        case t: Coin => (Some(PrinterState("¢" + thisJoinPoint.point.value, thisJoinPoint.point.value, false), Lambda), thisNFA)
        case _ => (None, thisNFA)
      }
    })
  }
}
