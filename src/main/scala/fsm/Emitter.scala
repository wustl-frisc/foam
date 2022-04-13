package edu.wustl.sbs
package fsm

import scala.collection.mutable.Map
import com.liangdp.graphviz4s.Digraph

object Emitter {
  def apply(fsm: FSM, namer: Any => String, excludeError: Boolean = false, excludeDisconnected: Boolean = false) = {

    val dot = new Digraph("finite_state_machine")

    dot.node(" ", attrs=Map("shape" -> "plain"))
    dot.edge(" ", namer(fsm.start))

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
    for (state <- states & fsm.accept) {
      dot.node(namer(state), attrs = Map("shape" -> "doublecircle"))
    }
    for (state <- (states -- fsm.accept)) {
      if (state != fsm.error || !excludeError) {
        dot.node(namer(state), attrs = Map("shape" -> "circle"))
      }
    }

    dot.view(fileName = "fsm.gv", directory = ".")
  }
}
