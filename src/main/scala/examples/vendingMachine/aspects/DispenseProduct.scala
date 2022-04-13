package edu.wustl.sbs
package examples

import fsm._
import aspects._

class DispenseProduct(product: Product) extends Aspect[NFA] {
  def apply(nfa: NFA) = {
    val statePointCut: Pointcut[ValueState] = Pointcutter[State, ValueState](nfa.states, state => state match {
      case s: ValueState if(s.value >= product.value) => true
      case _ => false
    })

    val nfaWithDispensing = Following[ValueState](statePointCut, nfa)((thisJoinPoint: State) => (product, DispenseState(product)))
    Following[State](Set[State](DispenseState(product)), nfaWithDispensing)((s: State) => (Lambda, nfaWithDispensing.acceptState))
  }
}
