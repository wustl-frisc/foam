package edu.wustl.sbs
package main
import fsm._
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

  VendingMachine()

  // val vendFSM = VendingMachine()
  // vendFSM.execute(List[Token](Coin(5), Coin(25), Product(30, "Gum")))

  // val dto = DetectTwoOnes.fsm
  // dto.execute(List(Zero, One, Zero, One, Zero, One, Zero, Zero, Zero, One, One))
  // (new ChiselStage).emitVerilog(
  //   new ChiselFSM(new ConvertedFSM(dto))
  // )

}
