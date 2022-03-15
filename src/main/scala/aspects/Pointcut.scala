package aspects
import featuredfsm._
import fsm._

import scala.util.matching.Regex

//use the factory pattern to create different pointcuts
object Pointcut {

  def byState(source: State)(implicit fsm: FeatureOrientedFSM) = {
    for(t <- fsm.alphabet) yield ((source, t))
  }

  def byToken(token: Token)(implicit fsm: FeatureOrientedFSM) = {
    for(s <- fsm.states) yield (s, token)
  }

  def byDestination(destination: State)(implicit fsm: FeatureOrientedFSM) = {
    fsm.transitions.foldLeft(Set[(State, Token)]())((keySet, m) => {
      if(m._2 contains destination) {
        keySet + m._1
      } else {
        keySet
      }
    })
  }
}
