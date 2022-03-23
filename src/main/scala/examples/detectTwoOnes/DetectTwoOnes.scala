package examples

import fsm._
import featuredfsm._
import aspects._

object DetectTwoOnes {

    val start = SimpleStateFactory()
    val acceptState = SimpleStateFactory()
    val error = SimpleStateFactory()
    implicit var fsm = FeatureOrientedFSM(start, acceptState, error)

    // Tokens
    fsm = fsm.addToken(Zero)
    fsm = fsm.addToken(One)

    // States
    val stateOne = SimpleStateFactory()
    fsm = fsm.addState(stateOne, "one")

    // Transitions from start
    fsm = fsm.addTransition((start, Zero), start)
    fsm = fsm.addTransition((start, One), stateOne)

    // Transitions from stateOne
    fsm = fsm.addTransition((stateOne, Zero), start)
    fsm = fsm.addTransition((stateOne, One), acceptState)

}