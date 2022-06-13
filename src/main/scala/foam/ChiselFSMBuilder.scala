package foam

import chisel3._

object ChiselFSMBuilder {
  def apply(dfa: DFA, boolMap: Map[Token, Bool], codeMap: Map[State, () => Unit]) = {

    val chiselFSM = Module(new ChiselFSM(dfa))
    
    val boolWithInt = (for(t <- dfa.alphabet) yield(boolMap(t), chiselFSM.tokenMap(t)))
    val stateIntWithCode = (for(s <- dfa.states) yield(chiselFSM.stateMap(s), codeMap(s)))


    for((cond, i) <- boolWithInt) {
      when(cond) {
        chiselFSM.io.in := i.U
      }
    }

    for((s, code) <- stateIntWithCode) {
      when(chiselFSM.io.out === s.U) {
        code()
      }
    }

    chiselFSM
  }
}
