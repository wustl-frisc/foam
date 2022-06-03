
package foam
package examples

trait HasWarning {
  this: Component =>
  val warning: String
  override def toString = super.toString + " " + warning
}
