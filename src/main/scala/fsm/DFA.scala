package edu.wustl.sbs
package fsm

trait DFA {
    def start: State
    def accept: Set[State]
    def states: Set[State]
    def alphabet: Set[Token]
    def transitions: Map[TransitionKey, State]
}

case class MultiState(s: Set[State]) extends State {

    override def executeCode: Unit = {
        for (state <- s) {
            state.executeCode
        }
    }

}

class ConvertedFSM(f: FSM) extends DFA {

    override def start: State = f.start
    private implicit var _accept: Set[State] = f.accept
    private implicit var _states: Set[State] = f.states
    override def alphabet: Set[Token] = f.alphabet
    private implicit var _transitions: Map[TransitionKey,State] = Map[TransitionKey,State]()

    for (((source, token) -> destination) <- f.transitions) {

        if (destination.size > 1) {
            val m = MultiState(destination)
            _states = _states + m
            _transitions = _transitions + ((source, token) -> m)
            if ((destination & f.accept).size > 0) {
                _accept = _accept + m
            }
            val transitionsFromM = f.transitions.filter((transition) => destination.contains(transition._1._1))
            for (((_, token2) -> destination2) <- transitionsFromM) {
                for (dest <- destination2) {
                    _transitions = _transitions + ((m, token2) -> dest)
                }
            }
        } else if (destination.size == 1) {
            _transitions = _transitions + ((source, token) -> destination.toList(0))
        }

    }

    override def accept = _accept
    override def states = _states
    override def transitions = _transitions

}
