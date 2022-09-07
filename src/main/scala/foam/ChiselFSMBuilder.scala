package foam

import chisel3._
import scala.math._

object ChiselFSMBuilder {
  def apply(fsm: FSM): (Map[String, Bool], Map[String, Bool]) = {

    val numStates = fsm.states.size
    val numTokens = fsm.alphabet.size

    val statesWidth = ceil(log(numStates)/log(2)).toInt
    val tokensWidth = ceil(log(numTokens)/log(2)).toInt

    val stateMap = fsm.states.zipWithIndex.toMap
    val tokenMap = (for(t <- fsm.alphabet) yield (t -> {
      val newWire = Wire(Bool())
      newWire := false.B
      newWire
    })).toMap

    val stateRegister = RegInit(stateMap(fsm.start).U(statesWidth.W))

    for ((state, stateId) <- stateMap) {
      when (stateId.U === stateRegister) {
        val transitionsFromState = fsm.transitions.filter((transition) => state == transition._1._1)

        for (((source,token), dest) <- transitionsFromState) {
          if(!dest.isEmpty) {
            when (tokenMap(token)) {
              stateRegister := stateMap(dest.toList(0)).U
            }
          }
        }
      }
    }

    val tokenIDtoWire = (for ((t,b) <- tokenMap) yield (t.id -> b)).toMap
    val stateIDtoWire = (for ((s,i) <- stateMap) yield (s.id -> (i.U === stateRegister))).toMap

    (tokenIDtoWire, stateIDtoWire)
  }
}