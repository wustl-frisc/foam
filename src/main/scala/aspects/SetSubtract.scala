package aspects

import featuredfsm._
import fsm._

object SetSubtract extends Aspect {
  def apply(pointcut: Set[(State, Token)], destinations: Set[State])(implicit fsm: FeatureOrientedFSM): FeatureOrientedFSM = {
    val newTransitions = for (key <- pointcut; d <- destinations) yield (key, d)
    newTransitions.foldLeft(fsm)((fsm, t) => fsm.removeTransition(t._1, t._2))
  }
}
