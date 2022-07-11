package foam.product

import foam.NFA

object CrossProduct {
  def apply(m1: NFA, m2: NFA, isConjunctionMachine: Boolean = true): NFA = {

    val combinedStartState = CombinedState(m1.start, m2.start, isConjunctionMachine)

    var oldMachine = new NFA(combinedStartState)
    var newMachine = oldMachine

    do {
      oldMachine = newMachine

      for(combinedSrcState <- oldMachine.states.asInstanceOf[Set[CombinedState]]) {
        val (s1, s2) = combinedSrcState.unApply

        // Get all transitions from s1 in machine 1 and s2 in machine 2
        val m1TransitionsFromS1 = m1.transitions.filter(_._1._1 == s1)
        val m2TransitionsFromS2 = m2.transitions.filter(_._1._1 == s2)

        // Create all transition edges from their cross product
        for(((_, m1TransitionToken), m1DstStates) <- m1TransitionsFromS1;
            ((_, m2TransitionToken), m2DstStates) <- m2TransitionsFromS2) {

          // Create the combined transition token
          val combinedToken = CombinedToken(m1TransitionToken, m2TransitionToken)

          // Create a transition from the src state to states in the cross product of destination states.
          for(m1DstState <- m1DstStates; m2DstState <- m2DstStates) {
            val combinedDstState = CombinedState(m1DstState, m2DstState, isConjunctionMachine)
            newMachine = newMachine.addTransition((combinedSrcState, combinedToken), combinedDstState)
          }
        }
      }
    } while (!oldMachine.isEqual(newMachine))

    newMachine
  }
}
