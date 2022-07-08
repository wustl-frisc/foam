package foam

import chisel3._
import scala.math._

object ChiselFSMBuilder {
  def apply(fsm: FSM) = {

    val numStates = fsm.states.size
    val numTokens = fsm.alphabet.size

    val statesWidth = ceil(log(numStates)/log(2)).toInt
    val tokensWidth = ceil(log(numTokens)/log(2)).toInt

    val stateMap = fsm.states.zipWithIndex.toMap
    val tokenMap = fsm.alphabet.zipWithIndex.toMap

    val stateRegister = RegInit(stateMap(fsm.start).U(statesWidth.W))

    for((s, i) <- stateMap) {
      when(stateRegister === i.U) {
        s.asInstanceOf[ChiselState].code()
      }
    }

    for ((state, stateId) <- stateMap) {
        when (stateId.U === stateRegister) {
            val transitionsFromState = fsm.transitions.filter((transition) => state == transition._1._1)
            for (((source,token), dest) <- transitionsFromState) {
              if(!dest.isEmpty) {
                when (token.asInstanceOf[ChiselToken].cond) {
                    stateRegister := stateMap(dest.toList(0)).U
                }
              }
            }
        }
    }
  }
}