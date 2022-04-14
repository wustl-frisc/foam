package edu.wustl.sbs
package examples

import fsm._
import aspects._

class AddCoin(coin: Coin, threshold: Int) extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    //select all the states of type ValueState
    val statePointCut: Pointcut[ValueState] = Pointcutter[State, ValueState](nfa.states, state => state match {
      case s: ValueState if(s.value + coin.value <= threshold) => true
      case _ => false
    })

    After[ValueState](statePointCut, nfa)((thisJoinPoint: ValueState) => (coin, ValueState(thisJoinPoint.value + coin.value)))
  }
}
