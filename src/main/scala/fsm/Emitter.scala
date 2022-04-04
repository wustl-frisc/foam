package edu.wustl.sbs
package fsm

import scala.collection.mutable.Map
import com.liangdp.graphviz4s.Digraph

object Emitter {
  def apply(fsm: FSM, namer: Any => String, excludeError: Boolean = false) = {

    val dot = new Digraph("finite_state_machine")

    //dot.attr("node", Map("shape" -> "doublecircle"))
    dot.attr("node", Map("shape" -> "circle"))

    fsm.transitions foreach (tm => {
      val key = tm._1
      val destinationSet = tm._2

      if((key._1 != fsm.error) || (key._1 == fsm.error && !excludeError)){
        dot.node(namer(key._1))
      }

      if(!(destinationSet contains fsm.error) || ((destinationSet contains fsm.error) && !excludeError)){
        destinationSet foreach (d => {
          dot.node(namer(d))
          dot.edge(namer(key._1), namer(d), label = namer(key._2))
        })
      }
    })

    dot.view(fileName = "fsm.gv", directory = ".")
  }
}
