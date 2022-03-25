package fsm.aspects

import fsm._
import fsm.featuredfsm._

trait Aspect {
  def apply(fsm: FeatureOrientedFSM): FeatureOrientedFSM
}
