package foam.examples.nim.aspects

import foam.{Lambda, NFA, State}
import foam.aspects.{AroundState, Aspect, Joinpoint, Pointcutter}
import foam.examples.nim.states.{HeapState, StartState}
import foam.examples.nim.traits.HeapBounds

class AddHeaps(heaps: Seq[HeapBounds]) extends Aspect[NFA] {

  override def apply(base: NFA): NFA = {
    val statePointCut = Pointcutter[State, StartState](base.states, {
      case _: StartState => true
      case _ => false
    })

    AroundState[StartState](statePointCut, base)((thisJoinPoint: Joinpoint[StartState], thisNFA: NFA) => {
      val newNFA = thisNFA.addTransition((thisJoinPoint.point, Lambda), HeapState(heaps.map(_.initial), heaps))
      (thisJoinPoint.point, newNFA)
    })
  }
}
