package foam.product

import foam.{MultiState, MultiStateFactory, MultiTokenFactory, NFA}

object CrossProduct {
  def apply(m1: NFA, m2: NFA, isConjunctionMachine: Boolean = true): NFA = {

    val combinedStartState = MultiStateFactory(Seq(m1.start, m2.start), isConjunctionMachine)

    // Each state stores one piece of information, representing one "sub-state." Whereas, a MultiState represents the
    // cross product of n >= 2 states together, representing n sub-states.
    val numSubStatesPerStateM1 = m1.start match {
      case MultiState(s, _) => s.size
      case _ => 1
    }

    var oldMachine = new NFA(combinedStartState)
    var newMachine = oldMachine

    do {
      oldMachine = newMachine

      for(combinedSrcState <- oldMachine.states.asInstanceOf[Set[MultiState]]) {

        // Get the states associated with machines m1 and m2, respectively, from the current combined state.
        val s1 = MultiStateFactory(combinedSrcState.s.take(numSubStatesPerStateM1), isConjunctionMachine)
        val s2 = MultiStateFactory(combinedSrcState.s.drop(numSubStatesPerStateM1), isConjunctionMachine)

        // Get all transitions from each machine.
        val m1TransitionsFromS1 = m1.transitions.filter(_._1._1 == s1)
        val m2TransitionsFromS2 = m2.transitions.filter(_._1._1 == s2)

        // Create all transitions from the cross product of edges from states s1 and s2 in machines m1 and m2, resp.
        for(((_, m1TransitionToken), m1DstStates) <- m1TransitionsFromS1;
            ((_, m2TransitionToken), m2DstStates) <- m2TransitionsFromS2) {

          val combinedTransitionToken = MultiTokenFactory(Seq(m1TransitionToken, m2TransitionToken))

          for(m1DstState <- m1DstStates; m2DstState <- m2DstStates) {
            val combinedDstState = MultiStateFactory(Seq(m1DstState, m2DstState), isConjunctionMachine)
            newMachine = newMachine.addTransition((combinedSrcState, combinedTransitionToken), combinedDstState)
          }
        }
      }

    } while (!oldMachine.isEqual(newMachine))

    newMachine
  }
}
