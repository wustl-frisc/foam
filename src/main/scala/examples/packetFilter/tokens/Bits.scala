package edu.wustl.sbs
package examples

import fsm._

case class Bits(val num: Int) extends Token {
  override def toString() = num.toString()
}