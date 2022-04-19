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

    val transitionPointcut: Pointcut[(ValueState, Token)] = for(s <- statePointCut) yield (s, product)

    val dispenseNFA = Around[(ValueState, Token)](transitionPointcut, nfa)((thisJoinPoint: (ValueState, Token)) => {
        nfa.getTransitions(thisJoinPoint) - nfa.error + DispenseState(product)
    })

    val dispensePointcut = Pointcutter[State, DispenseState](nfa.states, state => state match {
      case s: DispenseState => true
      case _ => false
    })

    val acceptPointcut: Pointcut[(DispenseState, Token)] = for(s <- dispensePointcut) yield (s, Lambda)

    Around[(DispenseState, Token)](acceptPointcut, dispenseNFA)((thisJoinPoint: (DispenseState, Token)) => {
      nfa.getTransitions(thisJoinPoint) - nfa.error + nfa.acceptState
    })

  }
}
