package aspects
import featuredfsm._
import fsm._

import scala.util.matching.Regex
import scala.reflect.ClassTag

//use the factory pattern to create different pointcuts
object Pointcut {

  def byState[A <: Token : ClassTag](source: State)(implicit fsm: FeatureOrientedFSM) = {
    for (t <- fsm.alphabet.collect{case t: A => t}) yield ((source, t))
  }

  def byState(source: Regex)(implicit fsm: FeatureOrientedFSM) = {
    for (t <- fsm.alphabet; s <- fsm.nameMap.filter(source matches _._1)) yield ((s._2, t))
  }

  def byToken[A <: State : ClassTag](token: Token, fsm: FeatureOrientedFSM) = {
    for (s <- fsm.states.collect{case s: A => s}) yield (s, token)
  }

  def byDestination(destination: State)(implicit fsm: FeatureOrientedFSM) = {
    fsm.transitions.foldLeft(Set[(State, Token)]())((keySet, m) => {
      if (m._2 contains destination) {
        keySet + m._1
      } else {
        keySet
      }
    })
  }

  def byDestination(destination: Regex)(implicit fsm: FeatureOrientedFSM) = {
    val stateSet = (for (s <- fsm.nameMap.filter(destination matches _._1)) yield (s._2)).toSet

    fsm.transitions.foldLeft(Set[(State, Token)]())((keySet, m) => {
      if (!(m._2 & stateSet).isEmpty) {
        keySet + m._1
      } else {
        keySet
      }
    })
  }
}
