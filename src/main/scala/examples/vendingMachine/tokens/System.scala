package edu.wustl.sbs
package examples

import fsm._

case class System(val action: String) extends Token {
  override def toString() = action
}
