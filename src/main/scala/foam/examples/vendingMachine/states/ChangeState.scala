
package foam
package examples


case class ChangeState(override val value: Int, override val isAccept: Boolean) extends ValueState(value, isAccept) with HasAction {

  override val action = "MakeChange"
}
