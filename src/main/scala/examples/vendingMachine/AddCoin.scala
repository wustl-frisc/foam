package edu.wustl.sbs
package examples

import fsm._
import aspects._

class AddCoin(coin: Coin, threshold: Int) extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    //select all the states of type ValueState
    val statePointCut: Pointcut[ValueState] = Pointcutter[State](nfa.states, state => state match {
      case s: ValueState => true
      case _ => false
    }) map {_.asInstanceOf[ValueState]}

    Advice[ValueState, NFA](statePointCut, nfa)((prevNFA, state) => {
      val total = state.value + coin.value
      if(total <= threshold) prevNFA.addTransition((state, coin), ValueState(total)) else prevNFA
    })
  }
}
