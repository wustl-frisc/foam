package edu.wustl.sbs
package examples

import fsm._
import aspects._

class DispenseProduct(product: Product) extends Aspect[NFA] {
  def apply(nfa: NFA) = {
    val statePointCut: Pointcut[ValueState] = Pointcutter[State, ValueState](nfa.states, state => state match {
      case s: ValueState  => true
      case _ => false
    })

    Around[ValueState](statePointCut, nfa)((thisJoinPoint: Joinpoint[ValueState], thisNFA: NFA) => {
      val noAcceptValueState = ValueState(thisJoinPoint.point.value, false)
      
      val newNFA = if(thisJoinPoint.point.value >= product.value) {
        thisNFA.addTransition((noAcceptValueState, product), DispenseState(product, product.value, true))
      } else {
        thisNFA.addTransition((noAcceptValueState, product), noAcceptValueState)
      }
      (noAcceptValueState, newNFA)
    })
  }
}
