package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class DispenseState(val product: Product, val source: Option[State], val isAccept: Boolean) extends State with HasAction with HasProduct with HasSource{
  override def executeCode = {
      CodeManager.signal(this)
  }

  override val action = "Dispense"
}
