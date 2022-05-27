package edu.wustl.sbs
package fsm
package examples

case class Product(val value: Int, val name: String) extends Token with HasName with HasValue {
  override def toString = name
}
