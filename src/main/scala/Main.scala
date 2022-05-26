package edu.wustl.sbs
package main
import fsm._
import aspects._
import fsm.featuredfsm._
import examples._
import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage

object Main extends App {

  val features = args.foldLeft(List[Aspect[NFA]]())((featureList, featureString) => { featureString match {
      case "BuyMore" => featureList :+ (new BuyMore)
      case "ChangeReturn" => featureList :+ (new ChangeReturn)
      case "InsufficientFunds" => featureList :+ (new InsufficientFunds)
      case "PeanutWarning" => featureList :+ (new PeanutWarning)
      case "PrintFunds" => featureList :+ (new PrintFunds)
  }})

  val vendFSM = VendingMachine(VendingMachine.USCoinSet,
    100,
    VendingMachine.GenericProducts,
    features)

  val vendDFA = new DFA(vendFSM, SimpleStateFactory(false))

  println(s"${vendDFA.states.size},${vendDFA.alphabet.size},${vendDFA.transitions.size}")

  (new ChiselStage).emitVerilog(new DFAWrapper(vendDFA))
}
