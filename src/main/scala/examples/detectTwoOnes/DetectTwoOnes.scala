package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._
import fsm.featuredfsm.aspects._

case class DetectTwoOnesFSM (val start: State, val acceptState: State, val error: State) extends NFA(start, acceptState, error)

object DetectTwoOnes {

    val start = SimpleStateFactory()
    val acceptState = SimpleStateFactory()

    val fsm = DetectTwoOnesFSM(start, acceptState, SimpleStateFactory())

    val stateOne = SimpleStateFactory()

    fsm.addTransition((start, Zero), start).
    addTransition((start, One), stateOne).
    addTransition((stateOne, Zero), start).
    addTransition((stateOne, One), acceptState)
}
