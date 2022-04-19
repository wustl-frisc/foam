package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class DispenseState(val product: Product) extends State with HasAction with HasProduct {
  override def executeCode = {
      CodeManager.signal(this)
  }

  override val action = "Dispense"
}
