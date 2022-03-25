package edu.wustl.sbs
package examples

import fsm._

case class Product(val value: Int, val name: String) extends Token {
  override def toString() = name
}
