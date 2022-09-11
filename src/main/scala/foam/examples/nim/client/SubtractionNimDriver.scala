package foam.examples.nim.client

import foam.NFA
import foam.aspects.{Aspect, Weaver}
import foam.examples.nim.NimWrapper
import foam.examples.nim.aspects._
import foam.examples.nim.traits.HeapBounds
import foam.examples.nim.utils.PropertiesReporter
import foam.product.CrossProduct

object SubtractionNimDriver {

  def subtractionNimMoveGenerator(heapBounds: Seq[HeapBounds], low: Int, high: Int): List[Aspect[NFA]] = {
    var features: List[Aspect[NFA]] = List()

    for (index <- heapBounds.indices; move <- low to high) {
      features = features.appended(new LegalMove(Seq(HeapModification(index, move))))
    }

    features
  }

  def apply(allowMovesBetween: (Int, Int) = (-1, -2), numPlayers: Int = 2): NFA = {
    val heapBounds = Seq(HeapBounds(21, 0))

    // Heap States
    var circleNimHeapFSMFeatures: List[Aspect[NFA]] = List(new AddHeaps(heapBounds))

    // Generate all legal moves for a standard game of nim.
    val low = if (allowMovesBetween._1 <= allowMovesBetween._2) allowMovesBetween._1 else allowMovesBetween._2
    val high = if (allowMovesBetween._1 <= allowMovesBetween._2) allowMovesBetween._2 else allowMovesBetween._1
    circleNimHeapFSMFeatures = circleNimHeapFSMFeatures ++ subtractionNimMoveGenerator(heapBounds, low, high)

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
