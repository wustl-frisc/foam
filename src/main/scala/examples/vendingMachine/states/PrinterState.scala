package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class PrinterState(val action: String, val isAccept: Boolean) extends State with HasAction {
  override def executeCode = {
      CodeManager.signal(this)
  }
}
