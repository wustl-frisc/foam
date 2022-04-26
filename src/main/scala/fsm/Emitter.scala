package edu.wustl.sbs
package fsm

import scala.collection.mutable.Map
import com.liangdp.graphviz4s.Digraph

object Emitter {
  def apply(fsm: FSM, namer: Any => String, excludeDisconnected: Boolean = false) = {

    val dot = new Digraph("finite_state_machine")

    dot.node(" ", attrs=Map("shape" -> "plain"))
    dot.edge(" ", namer(fsm.start))

    var reachableStates = Set[State]()

    for (((source, token) -> destinationSet) <- fsm.transitions) {
      for (destination <- destinationSet) {
        reachableStates += destination
        dot.edge(namer(source), namer(destination), label = namer(token))
      }
    }

    val states = if (excludeDisconnected) reachableStates else fsm.states
    for (state <- states.filter(_.isAccept)) dot.node(namer(state), attrs = Map("shape" -> "doublecircle"))
    for (state <- states.filter(_.isAccept == false)) dot.node(namer(state), attrs = Map("shape" -> "circle"))

    dot.view(fileName = "fsm.gv", directory = ".")
  }
}
