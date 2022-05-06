package edu.wustl.sbs
package examples

import fsm._
import aspects._

class BuyMore extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    val newNFA = (new SplitDispense)(nfa)

    val dispensePointcut = Pointcutter[State, DispenseState](newNFA.states, state => state match {
      case s: DispenseState if(s.value != 0) => true
      case _ => false
    })

    After[DispenseState](dispensePointcut, newNFA)((thisJoinPoint: Joinpoint[DispenseState], thisNFA: NFA) => {
      val fundsLeft = thisJoinPoint.point.value - thisJoinPoint.point.product.value

      thisJoinPoint.out match {
        case Some((t,s)) => (None, thisNFA)
        case None => (Some((Lambda, TotalState(fundsLeft, false))), thisNFA)
      }
    })
  }
}
