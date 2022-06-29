
package foam
package aspects

class Joinpoint[A <: Component](val point: A)

case class StateJoinpoint[A <: State](override val point: A,
                                      in: Option[TransitionKey],
                                      out: Option[(Token, State)]) extends Joinpoint(point)

case class TokenJoinpoint[A <: Token](override val point: A,
                                      in: State,
                                      out: State) extends Joinpoint(point)
