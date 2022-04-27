package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class PrinterState(val action: String, val source: Option[State], val isAccept: Boolean) extends State with HasAction with HasSource {
  override def executeCode = {
      CodeManager.signal(this)
  }
}
