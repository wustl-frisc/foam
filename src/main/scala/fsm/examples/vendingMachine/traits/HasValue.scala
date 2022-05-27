package edu.wustl.sbs
package fsm
package examples

trait HasValue {
  this: Component =>
  val value: Int
  override def toString = super.toString + " " + value
}
