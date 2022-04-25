package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class SrcIP3(val num: Int) extends State {
  override def executeCode = {
      CodeManager.signal(this)
  }

  override def toString = "Source X.X." + num.toString() + ".?"

}