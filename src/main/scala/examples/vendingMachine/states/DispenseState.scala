package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class DispenseState(val product: Product) extends State {
  override def executeCode = {
      CodeManager.signal(this)
  }

  override def toString = "Dispense" + product.toString

}
