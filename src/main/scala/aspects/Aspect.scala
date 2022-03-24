package aspects

import featuredfsm._

trait Aspect {
  def apply(fsm: FeatureOrientedFSM): FeatureOrientedFSM
}
