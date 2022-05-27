package edu.wustl.sbs
package fsm
package examples

trait HasWarning {
  this: Component =>
  val warning: String
  override def toString = super.toString + " " + warning
}
