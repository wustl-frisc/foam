package featuredfsm

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
        val out = Output(Bool())
    })

    val stateRegister = RegInit(stateMap(fsm.start).U(statesWidth.W))

    for ((state, stateId) <- stateMap) {
        when (stateId.U === stateRegister) {
            state.executeCode
            val transitionsFromState = fsm.transitions.filter((transition) => state == transition._1._1)
            for (((source,token), dest) <- transitionsFromState) {
                // TODO: This needs to be converted to a DFA first, right now it will produce mulitple assignments
                when (io.in === tokenMap(token).U) {
                    stateRegister := stateMap(fsm.transitions(state, token)).U
                }
                // for (destination <- fsm.transitions(state, token)){
                //     when (io.in === index.U) {
                //         stateRegister := stateMap(destination).U
                //     }
                // }
            }
        }
    }

    // io.out := fsm.accept.map((state) => stateRegister === stateMap(state).U).reduce(_ || _)
    io.out := fsm.accept.foldLeft(false.B)((prev, state) => prev || (stateRegister === stateMap(state).U))
}
