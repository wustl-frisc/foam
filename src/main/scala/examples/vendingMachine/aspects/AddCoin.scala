package edu.wustl.sbs
package examples

import fsm._
import aspects._

class AddCoin(coin: Coin, threshold: Int) extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    //select all the states of type ValueState
    val statePointCut = Pointcutter[State, ValueState](nfa.states, state => state match {
      case s: ValueState if(s.value + coin.value <= threshold) => true
      case _ => false
    })

    Around[ValueState](statePointCut, nfa)((thisJoinPoint: Joinpoint[ValueState], thisNFA: NFA) => {
      val newNFA = thisNFA.addTransition((thisJoinPoint.point, coin), ValueState(thisJoinPoint.point.value + coin.value, true))
      (thisJoinPoint.point, newNFA)
    })
  }
}
