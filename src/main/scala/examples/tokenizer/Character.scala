package edu.wustl.sbs
package examples

import fsm._

final case class Character(c: Char) extends Token {
  override def toString() = { c.toString() }
}
