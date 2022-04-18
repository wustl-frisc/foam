package edu.wustl.sbs
package examples

import fsm._

case class Product(val value: Int, val name: String) extends Token with HasName with HasValue {
  override def toString = name
}
