package foam.examples.nim.states

import foam.State

case class WinState(isWin: Boolean, playerNum: Int) extends State {
  override def isAccept: Boolean = true
  override def toString: String = s"Player ${(playerNum + 'A'.toInt).toChar} " + (if (isWin) "wins" else "loses")
}
