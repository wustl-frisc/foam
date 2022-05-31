
package foam
package examples

case class PrinterState(val action: String, override val value: Int, override val isAccept: Boolean)
  extends ValueState(value, isAccept) with HasAction
