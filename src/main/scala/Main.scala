package edu.wustl.sbs
package main
import fsm._
import fsm.featuredfsm._
import examples._
import chisel3.stage.ChiselStage

object Main extends App {

  /*
  val tokenizer = Tokenizer.fsm

  println("\n\n")
  tokenizer.execute(List[Token](Character('0'), Character('x'), Character('1'), Character('2'), Character(' ')))
  println("\n")
  tokenizer.execute(List[Token](Character('0'), Character('1'), Character('2'), Character(' ')))
  println("\n")
  tokenizer.execute(List[Token](Character('x'), Character('0'), Character('1'), Character('2'), Character(' ')))
  println("\n\n")
  */

  val vendFSM = VendingMachine()
  Emitter(vendFSM)

  val dto = DetectTwoOnes.fsm
  (new ChiselStage).emitVerilog(
    new ChiselFSM(new ConvertedFSM(vendFSM))
  )

}
