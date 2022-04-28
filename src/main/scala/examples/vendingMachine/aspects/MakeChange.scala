package edu.wustl.sbs
package examples

import fsm._
import aspects._

class MakeChange extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    val newNFA = (new SplitDispense)(nfa)

    val dispensePointcut = Pointcutter[State, DispenseState](newNFA.states, state => state match {
      case s: DispenseState => s.source match {
        case None => false
        case Some(source) => true
      }
      case _ => false
    })

    After[DispenseState](dispensePointcut, newNFA)((thisJoinPoint: Joinpoint[DispenseState], thisNFA: NFA) => {
      val source = thisJoinPoint.point.source.get.asInstanceOf[ValueState]
      val fundsLeft = source.value - thisJoinPoint.point.product.value

      thisJoinPoint.out.get match {
        case (s,t) if(s == thisJoinPoint.point && t == Lambda) => (None, thisNFA)
        case _ => (Some((Lambda, ChangeState(fundsLeft, true))), thisNFA)
      }
    })
  }
}
