package fsm

final case class Transition(var token: Token, var destination: State)