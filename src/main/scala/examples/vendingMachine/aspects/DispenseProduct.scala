package edu.wustl.sbs
package examples

import fsm._
import aspects._

class DispenseProduct(product: Product) extends Aspect[NFA] {
  def apply(nfa: NFA) = {
    val statePointCut: Pointcut[ValueState] = Pointcutter[State](nfa.states, state => state match {
      case s: ValueState if(s.value >= product.value) => true
      case _ => false
    }) map {_.asInstanceOf[ValueState]}

    val nfaWithDispensing = Following[ValueState](statePointCut, nfa)((thisJoinPoint: State) => (product, DispenseState(product)))
    Following[DispenseState](Set[DispenseState](DispenseState(product)), nfaWithDispensing)((s: State) => (Lambda, nfaWithDispensing.acceptState))
  }
}
