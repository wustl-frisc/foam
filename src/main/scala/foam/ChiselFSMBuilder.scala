package foam

import chisel3._
import scala.math._

class ChiselFSMHandle (val tokenIDtoWire: Map[String, Bool], val stateIDtoWire: Map[String, Bool]) {
  def apply(name: String): Bool = (tokenIDtoWire ++ stateIDtoWire)(name)
}

object ChiselFSMBuilder {
  def apply(fsm: FSM): ChiselFSMHandle = {

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
    println("start: " + fsm.start.toString)

    for ((state, stateId) <- stateMap) {
      println(state.toString + " " + stateId)
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

    new ChiselFSMHandle(tokenIDtoWire, stateIDtoWire)
  }
}