
package foam


class DFA(input: FSM, error: State) extends FSM {

    override def start: State = input.start
    override def alphabet = input.alphabet

    //get rid of unreachable states here
    private var newStates: Set[State] = input.transitions.foldLeft(Set[State](start))((prevSet, transition) => {
      prevSet ++ transition._2
    })
    private var newTransitions: Map[TransitionKey,Set[State]] = input.transitions
    addTransitionsToError()
    newStates = newStates + error
    processTransitions(newTransitions)

    override def states = newStates
    override def transitions = newTransitions

    private def processTransitions(transitions: Map[TransitionKey, Set[State]]): Unit = {
        for (((source, token) -> destination) <- transitions) {
            if (destination.size > 1) {
                val m = MultiStateFactory(destination)
                newTransitions = newTransitions + ((source, token) -> Set[State](m))
                if (!states.contains(m)) {
                    newStates = newStates + m
                    val transitionsFromM = input.transitions.filter({ case ((s, t), d) => destination.contains(s)})
                    processTransitions(reduceTransitionsByToken(transitionsFromM, m))
                }
            }
            else if (destination.size == 1) {
                newTransitions = newTransitions + ((source, token) -> destination)
            }
        }
    }

    private def reduceTransitionsByToken(transitions: Map[TransitionKey, Set[State]], state: State) = {
        implicit var tokenMap = Map[Token, Set[State]]()
        for (((source, token) -> destination) <- transitions) {
            tokenMap = tokenMap + (token -> (tokenMap.getOrElse(token, Set[State]()) ++ destination))
        }
        tokenMap.map({ case (t, d) => ((state, t), d) })
    }

    private def addTransitionsToError() = {
        for (token <- alphabet - Lambda) {
            for (state <- newStates) {
                val destOption = newTransitions(state, token)
                if (destOption.isEmpty) {
                    newTransitions += ((state, token) -> Set(error))
                }
            }
        }
    }
}
