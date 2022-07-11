package foam.product

import foam.State

case class CombinedState(s1: State, s2: State, conjunction: Boolean) extends State {
  override def isAccept: Boolean = if (conjunction) s1.isAccept && s2.isAccept else s1.isAccept || s2.isAccept
  override def toString: String = s1.toString + "," + s2.toString
}