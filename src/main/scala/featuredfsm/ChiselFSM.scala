package featuredfsm

import chisel3._
import chisel3.util.{switch, is}
import chisel3.experimental.ChiselEnum

import scala.math._

import fsm._

class ChiselFSM(fsm: FSM) extends Module {

    val numStates = fsm.states.size
    val numTokens = fsm.alphabet.size

    val statesWidth = ceil(log(numStates)/log(2)).toInt
    val tokensWidth = ceil(log(numTokens)/log(2)).toInt

    var i = 0;
    val stateMap = fsm.states.map((state: State) => {i+= 1 ; (state, i)}).toMap
    val tokenMap = fsm.alphabet.zipWithIndex


    val io = IO(new Bundle {
        val in = Input(UInt(tokensWidth.W))
        val out = Output(Bool())
    })

    val stateRegister = RegInit(stateMap(fsm.start).U(statesWidth.W))

    for ((state, stateId) <- stateMap) {
        when (stateId.U === stateRegister) {
            state.executeCode
            for ((token,index) <- tokenMap) {
                // TODO: This needs to be converted to a DFA first, right now it will produce mulitple assignments
                // when (io.in === index.U) {
                //     stateRegister := stateMap(fsm.transitions(state, token)).U
                // }
                for (destination <- fsm.transitions(state, token)){
                    when (io.in === index.U) {
                        stateRegister := stateMap(destination).U
                    }
                }
            }
        }
    }

    // io.out := fsm.accept.map((state) => stateRegister === stateMap(state).U).reduce(_ || _)
    io.out := fsm.accept.foldLeft(false.B)((prev, state) => prev || (stateRegister === stateMap(state).U))
}
