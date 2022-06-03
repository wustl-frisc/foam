
package foam
package examples

trait HasName {
  this: Component =>
  val name: String
  override def toString = super.toString + " " + name
}
