package edu.wustl.sbs
package fsm
package examples

case class System(val action: String) extends Token with HasAction
