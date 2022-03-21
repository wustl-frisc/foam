package aspects

import featuredfsm._
import fsm._

object SetUnion extends Aspect {
  def apply(pointcut: Set[(State, Token)], destinations: Set[State], fsm: FeatureOrientedFSM): FeatureOrientedFSM = {
    val newTransitions = for (key <- pointcut; d <- destinations) yield (key, d)
    newTransitions.foldLeft(fsm)((fsm, t) => fsm.addTransition(t._1, t._2))
  }
}
