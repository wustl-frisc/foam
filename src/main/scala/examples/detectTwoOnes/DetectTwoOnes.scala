package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

object DetectTwoOnes {

    val start = SimpleStateFactory()
    val accept = SimpleStateFactory()
    val error = SimpleStateFactory()

    val nameMap = Map[State, String](start -> "start", accept -> "accept", error -> "error")

    def apply() = {

        implicit var fsm = new NFA(start, accept, error)

        val stateOne = SimpleStateFactory()

        fsm = fsm.addTransition((start, Zero), start).
        addTransition((start, One), stateOne).
        addTransition((stateOne, Zero), start).
        addTransition((stateOne, One), accept)

        Emitter(fsm, element => element match {
            case state: State if (nameMap contains state) => nameMap(state)
            case state: State => state.toString
            case token: Token => token.toString
        }, true)

        fsm
    }
    
}
