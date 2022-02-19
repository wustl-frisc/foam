package `example-client`
import fsm._

final case class FeatureOrientedFSM(val start: State, val acceptState: State, val states: Set[State], val alphabet: Set[Token], val transitions: Set[Transition]) extends FSM {

    override def accept: Set[State] = Set[State](acceptState)

    def addTransition(t: Transition): FeatureOrientedFSM = {
        // Adds both states and the token to machine if needed
        val newStates = states + t.source + t.destination
        val newAlphabet = alphabet + t.token
        val newTransitions = transitions + t
        FeatureOrientedFSM(start, acceptState, newStates, newAlphabet, newTransitions)
    }

    def moveTransition(t: Transition, dest: State): FeatureOrientedFSM = {
        // Adds dest to the machine if needed
        val newStates = states + dest
        val newTransitions = transitions - t + Transition(t.source, t.token, dest)
        FeatureOrientedFSM(start, acceptState, newStates, alphabet, newTransitions)
    }

    def changeToken(t: Transition, newToken: Token) = {
        // Adds newToken to the alphabet if needed
        val newAlphabet = alphabet + newToken
        val newTransitions = transitions - t + Transition(t.source, newToken, t.destination)
        FeatureOrientedFSM(start, acceptState, states, newAlphabet, newTransitions)
    }

    def insertFsm(s: State, fsm: FeatureOrientedFSM): FeatureOrientedFSM = {
        // Adds s to machine if needed
        var newStates = states + s
        
        // Move the former transitions of s to fsm.accept
        var newTransitions = Set[Transition]()
        for (transition <- transitions) {
            if (transition.source == s) {
                newTransitions += Transition(fsm.acceptState, transition.token, transition.destination)
            } else {
                newTransitions += transition
            }
        }

        // Give s a single transition to fsm.start
        newTransitions += Transition(s, new Lambda(), fsm.start)

        // Add all states and tokens from fsm to this
        newStates = newStates ++ fsm.states
        val newAlphabet = alphabet ++ fsm.alphabet

        FeatureOrientedFSM(start, acceptState, newStates, newAlphabet, newTransitions)
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

object FeatureOrientedFSM {
    def initiliaze(): FeatureOrientedFSM = {
        val start = new SimpleState()
        val acceptState = new SimpleState()
        FeatureOrientedFSM(start, acceptState, Set[State](acceptState, start), Set[Token](new Lambda()), Set[Transition]())
    }
}