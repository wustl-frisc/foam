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

    Around[ValueState](statePointCut, nfa)((thisJoinPoint: ValueState, thisNFA: NFA) => {
      val newNFA = if(thisJoinPoint.value >= product.value) {
        thisNFA.addTransition((thisJoinPoint, product), DispenseState(product, thisJoinPoint.value, true))
      } else {
        thisNFA.addTransition((thisJoinPoint, product), thisJoinPoint)
      }
      (ValueState(thisJoinPoint.value, false), newNFA)
    })
  }
}
