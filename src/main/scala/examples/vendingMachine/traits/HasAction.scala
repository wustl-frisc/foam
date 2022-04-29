package edu.wustl.sbs
package examples

import fsm._

trait HasAction {
  this: Component =>
  val action: String
  override def toString = super.toString + " " + action
}
