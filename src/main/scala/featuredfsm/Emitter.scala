package featuredfsm

import scala.collection.mutable.Map
import com.liangdp.graphviz4s.Digraph

object Emitter {
  def apply(fsm: FeatureOrientedFSM, excludeError: Boolean = false) = {
    val stateMap = fsm.nameMap.map(_.swap)

    val dot = new Digraph("finite_state_machine")

    //dot.body += "concentrate=true"
    //dot.attr("node", Map("shape" -> "doublecircle"))
    dot.attr("node", Map("shape" -> "circle"))

    fsm.transitions foreach (tm => {
      val key = tm._1
      val destinationSet = tm._2

      if(key._1 != fsm.error) dot.node(stateMap(key._1))

      if(!(destinationSet contains fsm.error)){
        destinationSet foreach (d => {
          dot.node(stateMap(d))
          dot.edge(stateMap(key._1), stateMap(d), label = key._2.toString)
        })
      }
    })

    dot.view(fileName = "fsm.gv", directory = ".")
  }
}
