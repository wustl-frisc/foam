package edu.wustl.sbs
package fsm
package featuredfsm

package object aspects {
  type Pointcut = Set[TransitionKey]
  type FSMAspect = Aspect[FeatureOrientedFSM]
  type FSMAdvice = Advice[Pointcut, FeatureOrientedFSM]
}
