package fsm

final case class Transition(val token: Token, val destination: State)