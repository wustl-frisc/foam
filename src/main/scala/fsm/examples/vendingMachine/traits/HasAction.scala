package edu.wustl.sbs
package fsm
package examples

trait HasAction {
  this: Component =>
  val action: String
  override def toString = super.toString + " " + action
}
