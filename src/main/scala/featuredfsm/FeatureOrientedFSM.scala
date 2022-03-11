package `featuredfsm`
import fsm._

final case class FeatureOrientedFSM private (
    val start: State,
    val acceptState: State,
    val error: State,
    val states: Set[State],
    val alphabet: Set[Token],
    val transitions: Set[Transition]
) extends FSM {

    override def accept: Set[State] = Set[State](acceptState)

    def addToken(t: Token) = if(alphabet contains t) {
        this
    } else {
      val newAlphabet = alphabet + t

      //construct a new transition for each existing state to the error state on this token
      val errorTransitions = for (s <- states) yield Transition(s, t, error)

      val newTransitions = errorTransitions ++ transitions

      FeatureOrientedFSM(start, acceptState, error, states, newAlphabet, newTransitions)
    }

    def addState(s: State) = if(states contains s) {
      this
    } else {
      val newStates = states + s

      //construct a new transition for each token from this state to the error state
      val errorTransitions = for(t <- alphabet) yield Transition(s, t, error)

      val newTransitions = errorTransitions ++ transitions

      FeatureOrientedFSM(start, acceptState, error, newStates, alphabet, newTransitions)
    }

    def addTransition(t: Transition) = if(((alphabet + Lambda) contains t.token) &&
      (states contains t.source) && (states contains t.destination)) {
      val newTransitions = transitions + t
      FeatureOrientedFSM(start, acceptState, error, states, alphabet, newTransitions)
    } else {
      this
    }

    def removeTransition(t: Transition) = if(!(transitions contains t)) {
      this
    } else {
      val newTransitions = transitions - t
      FeatureOrientedFSM(start, acceptState, error, states, alphabet, newTransitions)
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

        FeatureOrientedFSM(start, acceptState, error, newStates, newAlphabet, newTransitions)
    }

    // For purely example
    def execute(input: List[Token]): Set[State] = {
        start.executeCode(Lambda)
        val finalStates = executeHelper(input, start)
        if(finalStates contains acceptState) println("Execution Sucess!")
        finalStates
    }

    private def executeHelper(input: List[Token], s: State): Set[State] = {
      val token = if (input.length > 0) input.head else Lambda
      var finalStates = Set[State]()

      if(input.length == 0 && s == acceptState) {
        finalStates + s
      } else {
        for (t <- transitions) {
            if (t.source == s) {
                if (t.token.isLamda) {
                    t.destination.executeCode(Lambda)
                    finalStates = finalStates ++ executeHelper(input, t.destination)
                } else if (t.token == token) {
                    t.destination.executeCode(token)
                    finalStates = finalStates ++ executeHelper(input.tail, t.destination)
                }
            }
        }
        if (finalStates == Set[State]()) {
            finalStates += s
        }
        finalStates
      }
    }
}

object FeatureOrientedFSM {
    def apply(start: State, accept: State, error: State): FeatureOrientedFSM = {
        FeatureOrientedFSM(start, accept, error, Set[State](accept, start, error), Set[Token](), Set[Transition]())
    }
}
