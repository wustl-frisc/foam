package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class ValueState(val value: Int) extends State {
  override def executeCode = {
      CodeManager.signal(this)
  }

  override def toString = value.toString 

}