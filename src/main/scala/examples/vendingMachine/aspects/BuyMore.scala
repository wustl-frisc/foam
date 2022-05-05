package edu.wustl.sbs
package examples

import fsm._
import aspects._

class BuyMore extends Aspect[NFA] {
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

      println(thisJoinPoint.out)

      thisJoinPoint.out match {
        case Some((t,s)) if(t == Lambda && s == ValueState(fundsLeft, source.isAccept)) => (None, thisNFA)
        case None => (Some((Lambda, ValueState(fundsLeft, source.isAccept))), thisNFA)
      }
    })
  }
}
