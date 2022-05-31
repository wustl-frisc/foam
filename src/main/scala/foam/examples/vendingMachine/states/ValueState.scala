
package foam
package examples

class ValueState(val value: Int, val isAccept: Boolean) extends State with HasValue

case class TotalState(override val value: Int, override val isAccept: Boolean) extends ValueState(value, isAccept)
