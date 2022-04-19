package edu.wustl.sbs
package examples

import fsm._

trait HasName {
  this: Component =>
  val name: String
  override def toString = super.toString + " " + name
}
