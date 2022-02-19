package `example-client`
import fsm._

class FeatureOrientedFSM extends FSM {

    val start = new SimpleState()
    val acceptState = new SimpleState()
    var states = Set[State](start, acceptState)
    val alphabet = Set[Token](new Lambda())
    var transitions = Set[Transition]()

    override def accept: Set[State] = Set[State](acceptState)

    def addTransition(t: Transition) = {
        // Adds both states to machine if needed
        states += t.source
        states += t.destination

        transitions += t
    }

    def insertFsm(s: State, fsm: FeatureOrientedFSM) = {
        // Adds s to machine if needed
        states += s
        
        // Move the former transitions of s to fsm.accept
        var newTransitions = Set[Transition]()
        for (transition <- transitions) {
            if (transition.source == s) {
                newTransitions += Transition(fsm.acceptState, transition.token, transition.destination)
            } else {
                newTransitions += transition
            }
        }
        transitions = transitions ++ newTransitions 

        // Give s a single transition to fsm.start
        transitions += Transition(s, new Lambda(), fsm.start)

        // Add all states from fsm to this
        states = states ++ fsm.states
    }

    // For purely example
    def accept(input: Iterable[Token]): Set[State] = {
        val inIterater = input.iterator
        var currentstates = Set[State](start)
        var futurestates = currentstates
        while (futurestates != Set[State]()) {
            currentstates = futurestates
            futurestates = Set[State]()
            futurestates = acceptLambdas(currentstates)
            if (futurestates == Set[State]() && inIterater.hasNext) {
                futurestates = futurestates ++ acceptTokens(currentstates ++ futurestates, inIterater.next())
            }
        }
        currentstates
    }

    private def acceptLambdas(states: Set[State]): Set[State] = {
        var toReturn = Set[State]()
        for (transition <- transitions) {
            if (states.contains(transition.source) && transition.token.isLamda) {
                toReturn += transition.destination
            }
        }
        toReturn
    }

    private def acceptTokens(states: Set[State], t: Token): Set[State] = {
        var toReturn = Set[State]()
        for (transition <- transitions) {
            if (states.contains(transition.source) && transition.token == t) {
                toReturn += transition.destination
            }
        }
        toReturn
    }

}
