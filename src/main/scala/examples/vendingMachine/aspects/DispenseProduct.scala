package edu.wustl.sbs
package examples

import fsm._
import aspects._

class DispenseProduct(product: Product) extends Aspect[NFA] {
  def apply(nfa: NFA) = {
    val statePointCut: Pointcut[TotalState] = Pointcutter[State, TotalState](nfa.states, state => state match {
      case s: TotalState  => true
      case _ => false
    })

    AroundState[TotalState](statePointCut, nfa)((thisJoinPoint: Joinpoint[TotalState], thisNFA: NFA) => {
      val noAcceptTotalState = TotalState(thisJoinPoint.point.value, false)

      val newNFA = if(thisJoinPoint.point.value >= product.value) {
        thisNFA.addTransition((noAcceptTotalState, product), DispenseState(product, 0, true))
      } else {
        thisNFA.addTransition((noAcceptTotalState, product), noAcceptTotalState)
      }
      (noAcceptTotalState, newNFA)
    })
  }
}
