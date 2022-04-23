package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class DispenseState(val product: Product, val value: Int, val isAccept: Boolean) extends State with HasValue with HasAction with HasProduct {
  override def executeCode = {
      CodeManager.signal(this)
  }

  override val action = "Dispense"
}
