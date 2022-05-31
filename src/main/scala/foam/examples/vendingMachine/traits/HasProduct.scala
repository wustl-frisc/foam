
package foam
package examples

trait HasProduct {
  this: Component =>
  val product: Product
  override def toString = super.toString + " " + product.toString
}
