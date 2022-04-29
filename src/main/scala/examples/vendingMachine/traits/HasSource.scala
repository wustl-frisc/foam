package edu.wustl.sbs
package examples

import fsm._

trait HasSource {
  this: Component =>
  val source: Option[State]
  override def toString = source match {
    case Some(state) => super.toString + " " + state.toString
    case None => super.toString
  }
}
