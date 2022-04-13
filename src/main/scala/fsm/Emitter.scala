package edu.wustl.sbs
package fsm

import scala.collection.mutable.Map
import com.liangdp.graphviz4s.Digraph

object Emitter {
  def apply(fsm: FSM, namer: Any => String, excludeError: Boolean = false, excludeDisconnected: Boolean = false) = {

    val dot = new Digraph("finite_state_machine")

    //dot.attr("node", Map("shape" -> "doublecircle"))
    dot.attr("node", Map("shape" -> "circle"))

    var usedStates = Set[State]()

    for (((source ,token) -> destinationSet) <- fsm.transitions) {
      if (!destinationSet.contains(fsm.error) || !excludeError) {
        usedStates += source
        for (destination <- destinationSet) {
          usedStates += destination
          dot.edge(namer(source), namer(destination), label = namer(token))
        }
      }
    }

    val states = if (excludeDisconnected) usedStates else fsm.states
    for (state <- states) {
      if (state != fsm.error || !excludeError) {
        dot.node(namer(state))
      }
    }

    dot.view(fileName = "fsm.gv", directory = ".")
  }
}
