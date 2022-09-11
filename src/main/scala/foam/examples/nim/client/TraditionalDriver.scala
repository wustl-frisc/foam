package foam.examples.nim.client

import foam.NFA
import foam.aspects.{Aspect, Weaver}
import foam.examples.nim.NimWrapper
import foam.examples.nim.aspects.{AddHeaps, AddPlayers, HeapModification, LegalMove, WinType}
import foam.examples.nim.traits.HeapBounds
import foam.examples.nim.utils.PropertiesReporter
import foam.product.CrossProduct

object TraditionalDriver {

  def traditionalNimMoveGenerator(heapBounds: Seq[HeapBounds]): List[Aspect[NFA]] = {
    // Generate all legal moves for a standard game of nim.
    var features: List[Aspect[NFA]] = List()

    for((hb, index) <- heapBounds.zipWithIndex) {
      val isCountUp = hb.initial <= hb.target
      for(t <- Math.min(hb.target, hb.initial) to Math.max(hb.target, hb.initial)) {
        if (t != 0) {
          features = features.appended(new LegalMove(Seq(HeapModification(index, if (isCountUp) t else -t))))
        }
      }
    }

    features
  }

  def apply(heapUpperBounds: Seq[Int] = Seq(3, 4), numPlayers: Int = 2): NFA = {
    val heapBounds = for (upperBound <- heapUpperBounds) yield HeapBounds(upperBound, 0)

    // Heap States
    var traditionalNimHeapFSMFeatures: List[Aspect[NFA]] = List(new AddHeaps(heapBounds))

    // Generate all legal moves for a standard game of nim.
    traditionalNimHeapFSMFeatures = traditionalNimHeapFSMFeatures ++ traditionalNimMoveGenerator(heapBounds)

    // Construct the Heap Machine
    val heapFSM = NimWrapper(traditionalNimHeapFSMFeatures)

    // Player State
    val playerFSMFeatures: List[Aspect[NFA]] = List(
     new AddPlayers(numPlayers)
    )

    // Construct the Player Machine
    val playerFSM = NimWrapper(playerFSMFeatures)

    // Construct the Player x Heaps Machine
    val playersAndHeapsFSM = CrossProduct(playerFSM, heapFSM)

    // Add in Misère play
    val nimFSM = Weaver[NFA](List(new WinType(false)), playersAndHeapsFSM,
      (oldNFA: NFA, newNFA: NFA) => oldNFA.isEqual(newNFA))

    PropertiesReporter(heapFSM, playerFSM, playersAndHeapsFSM, nimFSM)

    nimFSM
  }

}
