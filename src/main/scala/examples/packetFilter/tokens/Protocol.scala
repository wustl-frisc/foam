package edu.wustl.sbs
package examples

import fsm._

case class Protocol(val name: String) extends Token {
  override def toString() = name
}