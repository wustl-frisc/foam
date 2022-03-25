package edu.wustl.sbs
package fsm
package featuredfsm

import os.stat

object CodeManager {

    var statesToFunctions = Map[State, () => Unit]()

    def signal(s: State) = {
        val f = statesToFunctions.get(s)
        if (f.isDefined)
            f.get.apply()
    }

    def addCode(s: State, f: () => Unit) = {
        statesToFunctions += (s -> f)
    }

}
