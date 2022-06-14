package foam

import chisel3._

object ChiselFSMBuilder {
  def apply(dfa: DFA) = {

    val chiselFSM = Module(new ChiselFSM(dfa))

    val (headToken, headIndex) = (chiselFSM.tokenMap - Lambda).head
    val choppedMap = chiselFSM.tokenMap - Lambda - headToken

    (choppedMap.foldLeft(when(headToken.asInstanceOf[ChiselToken].cond) {
      chiselFSM.io.in := headIndex.U
    })((whenBlock, k) => {
      val (t, i) = k
      whenBlock.elsewhen(t.asInstanceOf[ChiselToken].cond){
        chiselFSM.io.in := i.U
      }
    })).otherwise {
      chiselFSM.io.in := chiselFSM.tokenMap(Lambda).U
    }

    for((s, i) <- chiselFSM.stateMap) {
      when(chiselFSM.io.out === i.U) {
        s.asInstanceOf[ChiselState].code()
      }
    }

    chiselFSM
  }
}
