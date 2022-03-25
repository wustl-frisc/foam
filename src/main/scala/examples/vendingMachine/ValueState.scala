package fsm.examples

import fsm._
import fsm.featuredfsm._

case class ValueState(val total: Int) extends State {
  override def executeCode = {
      CodeManager.signal(this)
  }
}
