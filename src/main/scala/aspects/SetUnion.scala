package fsm.aspects

import fsm._
import fsm.featuredfsm._

object SetUnion {
  def apply(pointcut: Set[TransitionKey], destinations: Set[State], fsm: FeatureOrientedFSM): FeatureOrientedFSM = {
    val newTransitions = for (key <- pointcut; d <- destinations) yield (key, d)
    newTransitions.foldLeft(fsm)((fsm, t) => fsm.addTransition(t._1, t._2))
  }
}
