package foam.examples.nim.states

import foam.State

case class StartState() extends State {
  override val isAccept = false
  override def toString: String = "Start"
}
