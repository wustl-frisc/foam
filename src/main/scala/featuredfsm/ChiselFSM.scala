package featuredfsm

import chisel3._
import chisel3.util.{switch, is}
import chisel3.experimental.ChiselEnum

import scala.math._

 class ChiselFSM(f: FeatureOrientedFSM) extends Module {

    val numStates = f.states.size
    val numTokens = f.alphabet.size

    val statesWidth = ceil(log(numStates)/log(2)).toInt
    val tokensWidth = ceil(log(numStates)/log(2)).toInt

    // ********************************************************************************************************************************* //


    val io = IO(new Bundle {
        val in = Input(UInt(tokensWidth.W))
        val out = Output(Bool())
    })

    val state = RegInit(f.nameMap("start").asInstanceOf[SimpleState].id.U)

    for (s <- f.states) {
        when(s.asInstanceOf[SimpleState].id.U === state) {
            s.executeCode
            for ((t,i) <- f.alphabet.zipWithIndex) {
              //TODO: This needs to be converted to a DFA first, right now it will produce mulitple assignments
              for(d <- f.transitions(s, t)){
                when (io.in === i.U) {
                    state := d.asInstanceOf[SimpleState].id.U
                }
              }
            }
        }
    }

    io.out := state === f.accept.asInstanceOf[SimpleState].id.U
}
