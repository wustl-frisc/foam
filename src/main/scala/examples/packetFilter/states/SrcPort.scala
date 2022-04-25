package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class SrcPort(val num: Int) extends State {
  override def executeCode = {
      CodeManager.signal(this)
  }

  override def toString = "Source " + num.toString()

}
