package edu.wustl.sbs
package fsm


case class MultiState(s: Set[State]) extends State {

    override def executeCode: Unit = {
        for (state <- s) {
            state.executeCode
        }
    }

    override def toString: String = {
        val strSet = s.map(state => state.toString())
        val sortedStrList = strSet.toList.sortWith(_.compareTo(_) < 0)
        sortedStrList.reduceLeft(_ + " and " + _)
    }

}

class DFA(f: FSM, excludeError: Boolean = false) extends FSM {

    override def start: State = f.start
    override def alphabet = f.alphabet
    override def error = f.error

    private implicit var newAccept: Set[State] = f.accept
    private implicit var newStates: Set[State] = f.states
    private implicit var newTransitions: Map[TransitionKey,Set[State]] = Map[TransitionKey,Set[State]]()
    processTransitions(f.transitions)

    override def accept = newAccept
    override def states = newStates
    override def transitions = newTransitions


    private def processTransitions(transitions: Map[TransitionKey, Set[State]]): Unit = {
        for (((source, token) -> destsWithError) <- transitions) {
            val destination = if (excludeError) destsWithError - f.error else destsWithError
            if (destination.size > 1) {
                val m = MultiState(destination)
                newTransitions = newTransitions + ((source, token) -> Set[State](m))
                if (!states.contains(m)) {
                    newStates = newStates + m
                    if ((destination & f.accept).size > 0) {
                        newAccept = newAccept + m
                    }
                    implicit var transitionsFromM = f.transitions.filter({ case ((s, t), d) => destination.contains(s)})
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

}
