package foam.examples.nim

import foam.NFA
import foam.aspects.{Aspect, Weaver}
import foam.examples.nim.states.StartState

object NimWrapper {

  private val start = StartState()

  def apply(features: List[Aspect[NFA]]): NFA = {
    implicit val fsm: NFA = new NFA(start)

    val finalFSM = Weaver[NFA](features, fsm, (before: NFA, after: NFA) => before.isEqual(after))

    finalFSM
  }

}
