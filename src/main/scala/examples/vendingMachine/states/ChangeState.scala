package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class ChangeState(override val value: Int, override val isAccept: Boolean) extends ValueState(value, isAccept) with HasAction {
  override def executeCode = {
      CodeManager.signal(this)
  }

  override val action = "MakeChange"
}
