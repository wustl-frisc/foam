package edu.wustl.sbs
package examples

import fsm._

trait HasValue {
  this: Component =>
  val value: Int
  override def toString = super.toString + " " + value
}
