package fsm

import chisel3._
import chisel3.util.{switch, is}
import chisel3.experimental.ChiselEnum

import scala.math._

class ChiselFSM(f: FSM) extends Module {

    val numStates = f.states.size
    val numTokens = f.alphabet.size

    val statesWidth = ceil(log(numStates)/log(2)).toInt
    val tokensWidth = ceil(log(numStates)/log(2)).toInt

    var i = 0
    val stateMap = f.states.map((state: State) => {i+= 1 ; (state, i)}).toMap

    i = 0
    val tokenMap = f.alphabet.map((token: Token) => {i += 1 ; (token, i)}).toMap

    val initialMap = (1 to numStates + 1).foldLeft(Map[Int, List[Transition]]())(
        (map: Map[Int, List[Transition]], j: Int) => map + (j -> Nil))
    val transitionMap = f.transitions.foldLeft(initialMap)(
        (map: Map[Int, List[Transition]], transition: Transition) => {
            val index = stateMap.get(transition.source).get
            map + (index -> (transition::map.get(index).get))
        }
    )

    // ********************************************************************************************************************************* //


    val io = IO(new Bundle {
        val in = Input(UInt(tokensWidth.W))
        val out = Output(Bool())
    })

    val state = RegInit(stateMap.get(f.start).get.U)
    
    for (s <- f.states) {
        val index = stateMap.get(s).get
        when(state === index.U) {
            // state.executeCode()              // Does this need to be reworked??
            for (transition <- transitionMap.get(index).get) {
                val tokenId = tokenMap.get(transition.token).get
                when (io.in === tokenId.U) {
                    val newStateIndex = stateMap.get(transition.destination).get
                    state := newStateIndex.U
                }

            }

        }

    }

    io.out := false.B
    for (s <- f.accept) {
        val index = stateMap.get(s).get
        io.out := io.out | (state === index.U)
    }


}
