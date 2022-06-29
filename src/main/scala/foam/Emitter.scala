
package foam

import scala.collection.mutable.Map
import com.liangdp.graphviz4s.Digraph
import chisel3.stage.ChiselStage

object Emitter {
  def emitGV(fsm: FSM, namer: Any => String): String = {

    val dot = new Digraph("finite_state_machine")

    dot.node(" ", attrs=Map("shape" -> "plain"))
    dot.edge(" ", namer(fsm.start))

    for (((source, token) -> destinationSet) <- fsm.transitions) {
      if(fsm.states contains source) {
        for (destination <- destinationSet) {
          dot.edge(namer(source), namer(destination), label = namer(token))
        }
      }
    }

    for (state <- fsm.states.filter(_.isAccept)) dot.node(namer(state), attrs = Map("shape" -> "doublecircle"))
    for (state <- fsm.states.filter(_.isAccept == false)) dot.node(namer(state), attrs = Map("shape" -> "circle"))

    dot.view(fileName = "fsm.gv", directory = ".")
  }

  def emitVerilog(dfa: DFA): String = (new ChiselStage).emitVerilog(new ChiselFSM(dfa))
}
