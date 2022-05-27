package edu.wustl.sbs
package fsm
package examples

case class DispenseState(val product: Product, override val value: Int, override val isAccept: Boolean)
  extends ValueState(value, isAccept) with HasAction with HasProduct {

  override val action = "Dispense"
}
