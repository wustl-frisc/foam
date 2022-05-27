package edu.wustl.sbs
package fsm
package examples

import aspects._

class AddCoin(coin: Coin, threshold: Int) extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    //select all the states of type TotalState
    val statePointCut = Pointcutter[State, TotalState](nfa.states, state => state match {
      case s: TotalState if(s.value + coin.value <= threshold) => true
      case _ => false
    })

    AroundState[TotalState](statePointCut, nfa)((thisJoinPoint: Joinpoint[TotalState], thisNFA: NFA) => {
      val newNFA = thisNFA.addTransition((thisJoinPoint.point, coin), TotalState(thisJoinPoint.point.value + coin.value, true))
      (thisJoinPoint.point, newNFA)
    })
  }
}
