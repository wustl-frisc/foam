package edu.wustl.sbs
package main
import fsm._
import fsm.featuredfsm._
import examples._
import chisel3.stage.ChiselStage

object Main extends App {
  // val vendFsm = VendingMachine(VendingMachine.USCoinSet, 100, VendingMachine.GenericProducts)

  // val vendDfa = new DFA(vendFsm, true)

  // val namer: Any => String = (element) => element match {
  //   case state: State if (VendingMachine.nameMap contains state) => VendingMachine.nameMap(state)
  //   case MultiState(s) => {
  //       val strSet = s.map(namer(_))
  //       val sortedStrList = strSet.toList.sortWith(_.compareTo(_) < 0)
  //       sortedStrList.reduceLeft(_ + " and\n " + _)
  //   }
  //   case other => other.toString
  // }

  // Emitter(vendDfa, namer, true, true)

  val namer: Any => String = (element) => element match {
    case state: State if (PacketFilter.nameMap contains state) => PacketFilter.nameMap(state)
    case MultiState(s) => {
        val strSet = s.map(namer(_))
        val sortedStrList = strSet.toList.sortWith(_.compareTo(_) < 0)
        sortedStrList.reduceLeft(_ + " and\n " + _)
    }
    case other => other.toString
  }

  val packetFilter = PacketFilter()
  Emitter(packetFilter, namer, true, true)


}
