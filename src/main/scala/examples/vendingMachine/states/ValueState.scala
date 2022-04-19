package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class ValueState(val value: Int) extends State with HasValue {
  override def executeCode = {
      CodeManager.signal(this)
  }
}
