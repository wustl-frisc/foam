package edu.wustl.sbs
package main
import fsm._

import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage

class DFAWrapper(dfa: DFA) extends Module {
  val io = IO(new Bundle {
    val out = Output(Bool())
  })

  val chiselDFA = Module(new ChiselFSM(dfa))

  val countOn = true.B
  val (counterValue, counterWrap) = Counter(countOn, dfa.alphabet.size)

  chiselDFA.io.in := counterValue
  io.out := chiselDFA.io.out.orR
}
