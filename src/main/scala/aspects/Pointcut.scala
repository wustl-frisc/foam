package aspects
import featuredfsm._
import fsm._

import scala.util.matching.Regex

//use the factory pattern to create different pointcuts
object Pointcut {
  def byState(source: Option[State], token: Option[Token], destination: Option[State])(implicit fsm: FeatureOrientedFSM) = {
    //when we are provided None, just match to itself, so It's always true
    fsm.transitions.filter(t =>
      source.getOrElse(t.source) == t.source &&
      token.getOrElse(t.token) == t.token &&
      destination.getOrElse(t.destination) == t.destination)
  }

  def byName(source: Option[Regex], token: Option[Token], destination: Option[Regex])(implicit fsm: FeatureOrientedFSM) = {

    fsm.transitions.filter(t => {
      val sourceName = fsm.nameMap.find(_._2 == t.source)
      val destinationName = fsm.nameMap.find(_._2 == t.source)

      source.getOrElse(sourceName.get._1.r).matches(sourceName.get._1) &&
      token.getOrElse(t.token) == t.token &&
      destination.getOrElse(destinationName.get._1.r).matches(destinationName.get._1)
    })
  }
}
