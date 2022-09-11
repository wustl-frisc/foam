package foam.examples.nim.aspects

import foam.{NFA, State}
import foam.aspects.{AroundState, Aspect, Joinpoint, Pointcutter}
import foam.examples.nim.states.HeapState
import foam.examples.nim.tokens.ModifyHeapToken

case class HeapModification(heapIdx: Int, delta: Int) {

  val action: String = if (delta >= 0) "Add" else "Take"
  private val preposition = if (delta >= 0) "to" else "from"

  override def toString: String = s"${action} ${Math.abs(delta)} tokens\n${preposition} heap ${heapIdx}"
}

class LegalMove(modifications: Seq[HeapModification]) extends Aspect[NFA] {

  override def toString: String = modifications.mkString(", ")

  override def apply(base: NFA): NFA = {

    // Checks to ensure the current heap state respects the heap "scheme" -- i.e., each heap is between its initial and
    // target values.
    def isStateValidAfterModifications(s: HeapState): Boolean =
      modifications.forall(hm =>
        hm.heapIdx >= 0 && hm.heapIdx < s.currentHeapState.length &&                   // Ensure the heap index is valid
          (if(s.heapScheme(hm.heapIdx).target <= s.heapScheme(hm.heapIdx).initial)             // Check if counting down
            s.currentHeapState(hm.heapIdx) + hm.delta >= s.heapScheme(hm.heapIdx).target else  // Respect being >= bound
            s.currentHeapState(hm.heapIdx) + hm.delta <= s.heapScheme(hm.heapIdx).target       // Respect being <= bound
          )
      )

    // Only select HeapStates that may be modified by the given modification without violating their heap scheme.
    val statePointCut = Pointcutter[State, HeapState](base.states, {
      case s: HeapState if isStateValidAfterModifications(s) => true
      case _ => false
    })

    // Perform the around advice.
    AroundState[HeapState](statePointCut, base)((thisJoinPoint: Joinpoint[HeapState], thisNFA: NFA) => {
      val heapModifications = Array.fill[Int](thisJoinPoint.point.currentHeapState.length)(0)

      for (hm <- modifications) {
        heapModifications(hm.heapIdx) = hm.delta
      }

      val newHeapValue: Seq[Int] = thisJoinPoint.point.currentHeapState.zip(heapModifications).map(t => t._1 + t._2)

      val newNFA = thisNFA addTransition (
        (thisJoinPoint.point, ModifyHeapToken(modifications)),
        thisJoinPoint.point.copy(newHeapValue)
      )

      (thisJoinPoint.point, newNFA)
    })
  }
}
