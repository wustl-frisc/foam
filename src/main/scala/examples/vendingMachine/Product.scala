package fsm.examples

import fsm._

case class Product(val value: Int, val name: String) extends Token {
  override def toString() = name
}
