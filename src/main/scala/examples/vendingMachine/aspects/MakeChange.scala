package edu.wustl.sbs
package examples

import fsm._
import aspects._

class MakeChange extends Aspect[NFA] {
  def apply(nfa: NFA) = {
    val dispensePointcut = Pointcutter[State, DispenseState](nfa.states, state => state match {
      case s: DispenseState => true
      case _ => false
    })

    Around[DispenseState](dispensePointcut, nfa)((thisJoinPoint: Joinpoint[DispenseState], thisNFA: NFA) => {
      val source = thisJoinPoint.in.get._1 match {
        case s: PrinterState => s.source.get
        case s: State => s
      }

      val fundsLeft = source.asInstanceOf[ValueState].value - thisJoinPoint.point.product.value
      val newDispense = DispenseState(thisJoinPoint.point.product, Some(source), thisJoinPoint.point.isAccept)
      val newNFA = thisNFA.addTransition((newDispense, Lambda), ChangeState(fundsLeft, true))

      (newDispense, newNFA)
    })
  }
}
