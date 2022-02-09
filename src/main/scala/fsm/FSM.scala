package fsm

trait FSM {

    val start: State
    val accept: Set[State]
    var states: Set[State]
    var alphabet: Set[Token]
    
}
