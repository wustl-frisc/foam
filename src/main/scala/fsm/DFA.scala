package edu.wustl.sbs
package fsm


class DFA(input: FSM, error: State) extends FSM {

    override def start: State = input.start
    override def alphabet = input.alphabet

    private var newStates: Set[State] = input.states
    private var newTransitions: Map[TransitionKey,Set[State]] = input.transitions
    addTransitionsToError()
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
        for (token <- alphabet) {
            for (state <- newStates) {
                val destOption = newTransitions(state, token)
                if (destOption.isEmpty) {
                    newTransitions += ((state, token) -> Set(error))
                }
            }
        }
    }
}
