package edu.wustl.sbs
package fsm
package featuredfsm
package aspects

class SetUnion(destinations: Set[State]) extends FSMAdvice {
  def apply(pointcut: Set[TransitionKey], fsm: FeatureOrientedFSM): FeatureOrientedFSM = {
    val newTransitions = for (key <- pointcut; d <- destinations) yield (key, d)
    newTransitions.foldLeft(fsm)((fsm, t) => fsm.addTransition(t._1, t._2))
  }
}
