package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class ChangeState(val value: Int, val isAccept: Boolean) extends State with HasAction with HasValue {
  override def executeCode = {
      CodeManager.signal(this)
  }

  override val action = "MakeChange"
}
