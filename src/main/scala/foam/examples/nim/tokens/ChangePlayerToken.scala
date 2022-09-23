package foam.examples.nim.tokens

import foam.Token
import foam.examples.nim.traits.HasPlayer

case class ChangePlayerToken(playerNum: Int) extends Token with HasPlayer {
  override def toString: String = (playerNum + 'A'.toInt).toChar.toString
}
