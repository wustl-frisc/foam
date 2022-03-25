package edu.wustl.sbs
package fsm
package featuredfsm
package aspects

object SetSubtract {
  def apply(pointcut: Set[TransitionKey], destinations: Set[State], fsm: FeatureOrientedFSM): FeatureOrientedFSM = {
    val newTransitions = for (key <- pointcut; d <- destinations) yield (key, d)
    newTransitions.foldLeft(fsm)((fsm, t) => fsm.removeTransition(t._1, t._2))
  }
}
