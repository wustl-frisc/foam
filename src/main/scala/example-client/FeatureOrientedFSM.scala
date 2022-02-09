package `example-client`
import fsm._

class FeatureOrientedFSM extends FSM {

    val start = new SimpleState()
    val acceptState = new SimpleState()
    val accept = Set[State](acceptState)
    var stateMap = Map[String, State]("start" -> start, "accept" -> acceptState)
    var states = Set[State](start, acceptState)
    var alphabet = Set[Token](new Lambda())

    def addState(s: State) = {
        if (!states.iterator.contains(s)) {
            states += s
            if (s.getName() != null) {
                stateMap += s.getName() -> s
            } else {
                stateMap += states.size.toString() -> s
            }
        }
    }

    // Adds both states to machine if needed
    def addTransition(s: State, t: Transition) = {
        addState(s)
        addState(t.destination)
        s.transitions += t
    }

    // Adds s to machine if needed
    def insertFsm(s: State, fsm: FSM) = {
        addState(s)

        // Change s to have a single transition to fsm.start
        val transitions = s.transitions
        s.transitions = Set[Transition]()
        s.transitions += Transition(new Lambda(), fsm.start)
        
        // Move the former transitions of s to fsm.accept
        for (state <- fsm.accept) {
            state.transitions ++ transitions
        }

        // Add all states from fsm to this
        for (state <- fsm.states) {
            addState(state)
        }
    }

    // For purely example
    def accept(input: Iterable[Token]): Set[State] = {
        var currentStates = Set[State](start)
        for (t: Token <- input) {
            currentStates ++ acceptHelper(currentStates, t)
        }
        currentStates
    }

    private def acceptHelper(s: Set[State], t: Token): Set[State] = {
        var toReturn = Set[State]()
        for(s: State <- s) {
            for (transition: Transition <- s.transitions) {
                if (transition.token == t) {
                    toReturn += transition.destination
                }
            }
        }
        toReturn
    }

}
