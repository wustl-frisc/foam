package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

class ValueState(val value: Int, val isAccept: Boolean) extends State with HasValue {
  override def executeCode = {
      CodeManager.signal(this)
  }
}

case class TotalState(override val value: Int, override val isAccept: Boolean) extends ValueState(value, isAccept)
