package aspects
import fsm._
import featuredfsm._

abstract class Aspect {
  def apply(implicit fsm: FeatureOrientedFSM): FeatureOrientedFSM
}
