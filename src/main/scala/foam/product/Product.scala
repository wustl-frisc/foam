package foam.product

import foam.{MultiStateFactory, NFA}

object Product {
  def apply(m1: NFA, m2: NFA, isConjunctionMachine: Boolean = true): NFA = {

    assert(m1.alphabet == m2.alphabet, "The alphabet of both machines must be the same for a Product Machine")

    val combinedStartState = MultiStateFactory(Seq(m1.start, m2.start), isConjunctionMachine)

    var oldMachine = new NFA(combinedStartState)
    var newMachine = oldMachine

    do {
      oldMachine = newMachine

      for(((m1SrcState, m1Transition), m1DstStates) <- m1.transitions;
          ((m2SrcState, m2Transition), m2DstStates) <- m2.transitions) {

        if (m1Transition == m2Transition) {
          val combinedSrcState = MultiStateFactory(Seq(m1SrcState, m2SrcState), isConjunctionMachine)
          val transitionToken = m1Transition

          for (m1DstState <- m1DstStates; m2DstState <- m2DstStates) {
            val combinedDstState = MultiStateFactory(Seq(m1DstState, m2DstState), isConjunctionMachine)
            newMachine = newMachine.addTransition((combinedSrcState, transitionToken), combinedDstState)
          }
        }
      }
    } while (!oldMachine.isEqual(newMachine))

    newMachine
  }
}
