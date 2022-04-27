package edu.wustl.sbs
package examples

import fsm._
import aspects._

class PrintFunds extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    //select all the states of type ValueState
    val statePointCut = Pointcutter[State, ValueState](nfa.states, state => state match {
      case s: ValueState => true
      case _ => false
    })

    Before[ValueState](statePointCut, nfa)((thisJoinPoint: Joinpoint[ValueState], thisNFA: NFA) => {
      thisJoinPoint.in.get._2 match {
        case t: Coin => (Some(PrinterState("¢" + thisJoinPoint.point.value, None, false), Lambda), thisNFA)
        case _ => (None, thisNFA)
      }
    })
  }
}
