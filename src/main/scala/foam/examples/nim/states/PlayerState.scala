package foam.examples.nim.states

import foam.State
import foam.examples.nim.traits.HasPlayer

case class PlayerState(playerNum: Int) extends State with HasPlayer {
  override val isAccept: Boolean = true
  override def toString: String = s"Player ${(playerNum + 'A'.toInt).toChar}"
}
