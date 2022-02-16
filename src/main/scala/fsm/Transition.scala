package fsm

final case class Transition(val source: State, val token: Token, val destination: State)