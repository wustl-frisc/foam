package aspects

import featuredfsm._

trait Aspect {
  def apply(implicit fsm: FeatureOrientedFSM) = fsm
}
