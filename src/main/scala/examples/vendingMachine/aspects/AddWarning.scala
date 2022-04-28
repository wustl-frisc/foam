package edu.wustl.sbs
package examples

import fsm._
import aspects._

class AddWarning(pointcut: Pointcut[State], warningString: String) extends Aspect[NFA] {
  def apply(nfa: NFA) = {
    Before[State](pointcut, nfa)((thisJoinPoint: Joinpoint[State], thisNFA: NFA) => {
      val source = thisJoinPoint.in.get._1
      val warning = PrinterState(warningString, Some(source), false)

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
