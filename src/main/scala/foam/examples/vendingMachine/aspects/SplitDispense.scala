
package foam
package examples

import aspects._

class SplitDispense extends Aspect[NFA] {
  def apply(nfa: NFA) = {
    val dispensePointcut = Pointcutter[State, DispenseState](nfa.states, state => state match {
      case s: DispenseState if(s.value == 0) => true
      case _ => false
    })

    AroundState[DispenseState](dispensePointcut, nfa)((thisJoinPoint: StateJoinpoint[DispenseState], thisNFA: NFA) => {

      val newDispense = DispenseState(thisJoinPoint.point.product,
        thisJoinPoint.in.get._1.asInstanceOf[ValueState].value,
        thisJoinPoint.point.isAccept)

      (newDispense, thisNFA)
    })
  }
}
