package edu.wustl.sbs
package fsm

import chisel3._
import chisel3.util.{switch, is}
import chisel3.experimental.ChiselEnum

import scala.math._

import fsm._

class ChiselFSM(fsm: DFA) extends Module {

    val numStates = fsm.states.size
    val numTokens = fsm.alphabet.size

    val statesWidth = ceil(log(numStates)/log(2)).toInt
    val tokensWidth = ceil(log(numTokens)/log(2)).toInt

    val stateMap = fsm.states.zipWithIndex.toMap
    val tokenMap = fsm.alphabet.zipWithIndex.toMap


    val io = IO(new Bundle {
        val in = Input(UInt(tokensWidth.W))
        val out = Output(UInt(statesWidth.W))
    })

    val stateRegister = RegInit(stateMap(fsm.start).U(statesWidth.W))

    for ((state, stateId) <- stateMap) {
        when (stateId.U === stateRegister) {
            val transitionsFromState = fsm.transitions.filter((transition) => state == transition._1._1)
            for (((source,token), dest) <- transitionsFromState) {
                when (io.in === tokenMap(token).U) {
                    stateRegister := stateMap(dest.toList(0)).U
                }
            }
        }
    }

    io.out := stateRegister
}
