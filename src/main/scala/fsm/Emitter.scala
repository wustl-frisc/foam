package edu.wustl.sbs
package fsm

import scala.collection.mutable.Map
import com.liangdp.graphviz4s.Digraph

object Emitter {
  def apply(fsm: FSM, namer: Any => String, excludeError: Boolean = false) = {

    val dot = new Digraph("finite_state_machine")

    //dot.attr("node", Map("shape" -> "doublecircle"))
    dot.attr("node", Map("shape" -> "circle"))

    for (state <- fsm.states) {
      if (state != fsm.error || !excludeError) {
        dot.node(namer(state))
      }
    }

    for (((source ,token) -> destinationSet) <- fsm.transitions) {
      if (!destinationSet.contains(fsm.error) || !excludeError) {
        for (destination <- destinationSet) {
          dot.edge(namer(source), namer(destination), label = namer(token))
        }
      }
    }

    dot.view(fileName = "fsm.gv", directory = ".")
  }
}
