package edu.wustl.sbs
package examples

import fsm._

case class Coin(val value: Int) extends Token with HasValue
