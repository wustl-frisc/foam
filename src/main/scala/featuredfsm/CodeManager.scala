package featuredfsm
import fsm._
import os.stat

object CodeManager {

    var statesToFunctions = Map[State, Token => Unit]()

    def signal(s: State, t: Token) = {
        val f = statesToFunctions.get(s)
        if (f.isDefined)
            f.get.apply(t)
    }

    def addCode(s: State, f: Token => Unit) = {
        statesToFunctions += (s -> f)
    }

}
