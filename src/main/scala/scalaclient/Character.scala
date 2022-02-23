package `scalaclient`
import fsm._

final case class Character(c: Char) extends Token { 
    override def toString() = {c.toString()}
}