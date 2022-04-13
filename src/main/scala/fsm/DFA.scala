package edu.wustl.sbs
package fsm


class DFA(input: FSM, excludeError: Boolean = false) extends FSM {

    val f = new RemovedLambda(input)

    override def start: State = f.start
    override def alphabet = f.alphabet
    override def error = f.error

    private var newAccept: Set[State] = f.accept
    private var newStates: Set[State] = f.states
    private var newTransitions: Map[TransitionKey,Set[State]] = Map[TransitionKey,Set[State]]()
    processTransitions(f.transitions)

    override def accept = newAccept
    override def states = newStates
    override def transitions = newTransitions


    private def processTransitions(transitions: Map[TransitionKey, Set[State]]): Unit = {
        for (((source, token) -> destsWithError) <- transitions) {
            val destination = if (excludeError) destsWithError - f.error else destsWithError
            if (destination.size > 1) {
                val m = MultiStateFactory(destination)
                newTransitions = newTransitions + ((source, token) -> Set[State](m))
                if (!states.contains(m)) {
                    newStates = newStates + m
                    if ((destination & f.accept).size > 0) {
                        newAccept = newAccept + m
                    }
                    val transitionsFromM = f.transitions.filter({ case ((s, t), d) => destination.contains(s)})
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


class RemovedLambda(f: FSM) extends FSM {

    override def error: State = f.error

    override def alphabet: Set[Token] = f.alphabet


    private val lambdaClosures = f.states.map((state) => (state, takeLambdaTransitions(state))).toMap
    private var newStates = f.states.map(state => MultiStateFactory(lambdaClosures(state)))
    private var newAccept = f.accept.map((state) => MultiStateFactory(lambdaClosures(state)))
    private var newTransitions: Map[TransitionKey,Set[State]] = Map[TransitionKey,Set[State]]()

    combineTransitions()

    override def states: Set[State] = newStates
    override def start = MultiStateFactory(lambdaClosures(f.start))
    override def accept = newAccept
    override def transitions = newTransitions

    private def takeLambdaTransitions(state: State): Set[State] = {
        val destination = f.transitions.filter({ case ((source, token), destination) => source==state && token.isLamda}).values.toSet.reduce(_++_)
        if (destination.size > 0) {
            destination.foldLeft(Set[State]())((acc, ele) => acc ++ takeLambdaTransitions(ele)) + state
        } 
        else {
            Set[State](state)
        }
    }

    private def combineTransitions() = {
        for ((_, closure) <- lambdaClosures) {
            for (token <- f.alphabet) {
                if (!token.isLamda) {
                    var destSet = Set[State]()
                    for (state <- closure) {
                        val destOption = f.transitions.get((state, token))
                        if (destOption.isDefined) {
                            destSet = destSet ++ destOption.get
                        }
                    }
                    val destination = if (destSet.size == 0) Set[State](f.error) else destSet.map((state) => MultiStateFactory(lambdaClosures(state)))
                    newTransitions = newTransitions + ((MultiStateFactory(closure), token) -> destination)
                }
            }
            if ((closure & f.accept).size > 0) {
                newAccept = newAccept + MultiStateFactory(closure)
            }
        }
    }

}