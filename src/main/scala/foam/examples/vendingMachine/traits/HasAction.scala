
package foam
package examples

trait HasAction {
  this: Component =>
  val action: String
  override def toString = super.toString + " " + action
}
