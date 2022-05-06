package edu.wustl.sbs
package aspects

import fsm._

class Joinpoint[A <: Component](val point: A)

case class StateJoinpoint[A <: State](override val point: A,
  val in: Option[TransitionKey],
  val out: Option[(Token, State)]) extends Joinpoint(point)

case class TokenJoinpoint[A <: Token](override val point: A,
  val in: State,
  val out: State) extends Joinpoint(point)
