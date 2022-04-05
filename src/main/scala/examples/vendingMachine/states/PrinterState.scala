package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class PrinterState(val display: String) extends State {
  override def executeCode = {
      CodeManager.signal(this)
  }

  override def toString = "Print" + display

}