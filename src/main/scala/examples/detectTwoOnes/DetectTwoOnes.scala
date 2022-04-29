 package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

object DetectTwoOnes {

    val start = SimpleStateFactory(false)

    val nameMap = Map[State, String](start -> "start")

    def apply() = {

        implicit var fsm = new NFA(start)

        val stateOne = SimpleStateFactory(false)
        val accept = SimpleStateFactory(true)

        fsm = fsm.addTransition((start, Zero), start).
        addTransition((start, One), stateOne).
        addTransition((stateOne, Zero), start).
        addTransition((stateOne, One), accept)

        fsm
    }

}
