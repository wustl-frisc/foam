
package foam
package examples

trait HasValue {
  this: Component =>
  val value: Int
  override def toString = super.toString + " " + value
}
