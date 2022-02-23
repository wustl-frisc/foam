package `featuredfsm`
import fsm._

final case class FeatureOrientedFSM(
    val start: State, 
    val acceptState: State, 
    val states: Set[State], 
    val alphabet: Set[Token], 
    val transitions: Set[Transition]
) extends FSM {

    override def accept: Set[State] = Set[State](acceptState)

    def addTransition(t: Transition) = {
        // Add both states and the token to machine if needed
        val newStates = states + t.source + t.destination
        val newAlphabet = alphabet + t.token
        val newTransitions = transitions + t
        FeatureOrientedFSM(start, acceptState, newStates, newAlphabet, newTransitions)
    }

    def moveTransition(t: Transition, dest: State) = {
        // Add dest to the machine if needed
        val newStates = states + dest
        val newTransitions = transitions - t + Transition(t.source, t.token, dest)
        FeatureOrientedFSM(start, acceptState, newStates, alphabet, newTransitions)
    }

    def changeToken(t: Transition, newToken: Token) = {
        // Add newToken to the alphabet if needed
        val newAlphabet = alphabet + newToken
        val newTransitions = transitions - t + Transition(t.source, newToken, t.destination)
        FeatureOrientedFSM(start, acceptState, states, newAlphabet, newTransitions)
    }

    def addCode(s: State, f: Token => Unit) = {
        s.body = f::s.body
        this
    }

    def insertFsm(s: State, fsm: FeatureOrientedFSM) = {
        // Add s to machine if needed
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
        newTransitions += Transition(s, Lambda, fsm.start)

        // Add all states and tokens from fsm to this
        newStates = newStates ++ fsm.states
        val newAlphabet = alphabet ++ fsm.alphabet

        FeatureOrientedFSM(start, acceptState, newStates, newAlphabet, newTransitions)
    }

    // For purely example
    def accept(input: List[Token]): Set[State] = {
        start.executeCode(Lambda)
        acceptHelper(input, start)
    }

    private def acceptHelper(input: List[Token], s: State): Set[State] = {
        val token = if (input.length > 0) input.head else Lambda
        var finalStates = Set[State]()
        for (t <- transitions) {
            if (t.source == s) {
                if (t.token.isLamda) {
                    t.destination.executeCode(Lambda)
                    finalStates = finalStates ++ acceptHelper(input, t.destination)
                } else if (t.token == token) {
                    t.destination.executeCode(token)
                    finalStates = finalStates ++ acceptHelper(input.tail, t.destination)
                }
            }
        }
        if (finalStates == Set[State]()) {
            finalStates += s
        }
        finalStates
    }

}

object FeatureOrientedFSM {
    def initiliaze(): FeatureOrientedFSM = {
        val start = new SimpleState()
        val acceptState = new SimpleState()
        FeatureOrientedFSM(start, acceptState, Set[State](acceptState, start), Set[Token](Lambda), Set[Transition]())
    }
}