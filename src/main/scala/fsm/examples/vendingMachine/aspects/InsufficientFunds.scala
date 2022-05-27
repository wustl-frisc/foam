package edu.wustl.sbs
package fsm
package examples

import aspects._

class InsufficientFunds extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    val tokenPointcut = Pointcutter[Token, Coin](nfa.alphabet, token => token match {
      case t: Product => true
      case _ => false
    })

    AfterToken[Coin](tokenPointcut, nfa)((thisJoinPoint: TokenJoinpoint[Coin], thisNFA: NFA) => {
      var value = thisJoinPoint.out.asInstanceOf[ValueState].value
      thisJoinPoint.out match {
        case s: TotalState => (Some((PrinterState("Insufficient Funds!", s.value, false), Lambda)), thisNFA)
        case _ => (None, thisNFA)
      }
    })
  }
}
