package foam.product

import foam.Token

case class CombinedToken(t1: Token, t2: Token) extends Token {
  override def toString: String = t1.toString + "," + t2.toString
  override def isLamda: Boolean = t1.isLamda && t2.isLamda
  def unApply: (Token, Token) = (t1, t2)
}