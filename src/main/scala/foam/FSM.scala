
package foam

trait FSM {
    def start: State
    def states: Set[State]
    def alphabet: Set[Token]
    def transitions: Map[TransitionKey, Set[State]]

    def isEqual(otherFSM: FSM) = {
      otherFSM.states.equals(this.states) &&
      otherFSM.alphabet.equals(this.alphabet) &&
      otherFSM.transitions.equals(this.transitions)
    }
}


// "Scala standard is to leave off the parens unless the method changes the object itself (rather than just returning something)"
