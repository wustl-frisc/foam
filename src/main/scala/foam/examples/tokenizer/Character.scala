
package foam
package examples

final case class Character(c: Char) extends Token {
  override def toString() = { c.toString() }
}
