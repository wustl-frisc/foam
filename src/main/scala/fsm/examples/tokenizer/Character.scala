package edu.wustl.sbs
package fsm
package examples

final case class Character(c: Char) extends Token {
  override def toString() = { c.toString() }
}
