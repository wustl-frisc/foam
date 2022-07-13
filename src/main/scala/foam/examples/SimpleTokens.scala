package foam.examples

import foam.Token

case class NumericToken(value: Int) extends Token {
  override def toString: String = value.toString
}

case class TextToken(value: String) extends Token {
  override def toString: String = value
}