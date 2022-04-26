package edu.wustl.sbs
package main
import fsm._
import fsm.featuredfsm._
import examples._
import chisel3.stage.ChiselStage

object Main extends App {
  val vendFsm = VendingMachine(VendingMachine.USCoinSet, 100, VendingMachine.GenericProducts)

  Emitter(vendFsm, VendingMachine.namer, true)
}
