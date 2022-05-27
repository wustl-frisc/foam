package edu.wustl.sbs
package fsm
package examples

import aspects._

class AddWarning(pointcut: Pointcut[ValueState], warningString: String) extends Aspect[NFA] {
  def apply(nfa: NFA) = {
    BeforeState[ValueState](pointcut, nfa)((thisJoinPoint: StateJoinpoint[ValueState], thisNFA: NFA) => {
      val source = thisJoinPoint.in.get._1.asInstanceOf[ValueState]
      val warning = PrinterState(warningString, source.value, false)

      source match {
        case s: PrinterState if(s.action == warningString) => (None, thisNFA)
        case _ => {
          val newNFA = thisNFA.addTransition((warning, System("Reject")), source)
          (Some(warning, System("Accept")), newNFA)
        }
      }
    })
  }
}
