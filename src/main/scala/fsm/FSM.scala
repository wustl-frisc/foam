package fsm

trait FSM {

    def start: State
    def accept: Set[State]
    def states: Set[State]
    def alphabet: Set[Token]
    def transitions: Set[Transition]
    
}


// "Scala standard is to leave off the parens unless the method changes the object itself (rather than just returning something)"