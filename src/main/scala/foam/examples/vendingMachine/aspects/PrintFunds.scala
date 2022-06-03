
package foam
package examples

import aspects._

class PrintFunds extends Aspect[NFA] {
  def apply(nfa: NFA) = {

    val tokenPointcut = Pointcutter[Token, Coin](nfa.alphabet, token => token match {
      case t: Coin => true
      case _ => false
    })

    AfterToken[Coin](tokenPointcut, nfa)((thisJoinPoint: TokenJoinpoint[Coin], thisNFA: NFA) => {
      var value = thisJoinPoint.out.asInstanceOf[ValueState].value
      thisJoinPoint.out match {
        case s: PrinterState if (s.action == "¢" + value.toString) => (None, thisNFA)
        case _ => (Some((PrinterState("¢" + value, value, false), Lambda)), thisNFA)
      }
    })
  }
}
