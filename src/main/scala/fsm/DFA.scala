package fsm

trait DFA {
    def start: State
    def accept: Set[State]
    def states: Set[State]
    def alphabet: Set[Token]
    def transitions: Map[(State, Token), State]
}
