package foam.examples.nim.client

import foam.NFA
import foam.aspects.{Aspect, Weaver}
import foam.examples.nim.NimWrapper
import foam.examples.nim.aspects._
import foam.examples.nim.traits.HeapBounds
import foam.examples.nim.utils.PropertiesReporter
import foam.product.CrossProduct

object CircleNimDriver {

  def circleNimMoveGenerator(heapBounds: Seq[HeapBounds]): List[Aspect[NFA]] = {
    var features: List[Aspect[NFA]] = List()

    def wrap(i: Int): Int = i % heapBounds.length

    for (startingIndex <- heapBounds.indices; k <- 1 to 3) {
      val move = for(index <- 1 to k) yield HeapModification(wrap(index + startingIndex), -1)
      features = features.appended(new LegalMove(move))
    }

    features
  }

  def apply(numHeaps: Int = 3, numPlayers: Int = 2): NFA = {
    val heapBounds = for (i <- 1 to numHeaps) yield HeapBounds(1, 0)

    // Heap States
    var circleNimHeapFSMFeatures: List[Aspect[NFA]] = List(new AddHeaps(heapBounds))

    // Generate all legal moves for a standard game of nim.
    circleNimHeapFSMFeatures = circleNimHeapFSMFeatures ++ circleNimMoveGenerator(heapBounds)

    // Construct the Heap Machine
    val heapFSM = NimWrapper(circleNimHeapFSMFeatures)

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
