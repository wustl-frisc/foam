package edu.wustl.sbs
package examples

import fsm._

trait HasWarning {
  this: Component =>
  val warning: String
  override def toString = super.toString + " " + warning
}
