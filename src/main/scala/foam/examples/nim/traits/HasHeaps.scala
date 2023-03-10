package foam.examples.nim.traits

case class HeapBounds(initial: Int, target: Int)

trait HasHeaps {
  val currentHeapState: Seq[Int]
  val heapScheme: Seq[HeapBounds]
}
