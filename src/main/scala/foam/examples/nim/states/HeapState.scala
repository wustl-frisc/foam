package foam.examples.nim.states

import foam.State
import foam.examples.nim.traits.{HasHeaps, HeapBounds}

case class HeapState(currentHeapState: Seq[Int], heapScheme: Seq[HeapBounds]) extends State with HasHeaps {
  override def isAccept: Boolean = currentHeapState.zip(heapScheme.map(_.target)).forall({
    case (value, target) => value == target
  })
  override def toString: String = "[" + currentHeapState.mkString(",") + "]"
}