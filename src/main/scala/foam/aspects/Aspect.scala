
package foam
package aspects

trait Aspect[A] {
  def apply(base: A): A
}
