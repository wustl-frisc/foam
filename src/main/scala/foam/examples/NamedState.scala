package foam.examples

import foam.State

case class NamedState(name: String, override val isAccept: Boolean) extends State {
  override def toString: String = name
}