package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

object DetectTwoOnes {

    val start = SimpleStateFactory()
    val acceptState = SimpleStateFactory()

    val fsm = new NFA(start, acceptState, SimpleStateFactory())

    val stateOne = SimpleStateFactory()

    fsm.addTransition((start, Zero), start).
    addTransition((start, One), stateOne).
    addTransition((stateOne, Zero), start).
    addTransition((stateOne, One), acceptState)
}
