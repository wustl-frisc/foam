package edu.wustl.sbs
package examples

import fsm._
import aspects._

class PeanutWarning extends Aspect[NFA] {
  def apply(nfa: NFA) = {
    val statePointCut = Pointcutter[State, State](nfa.states, state => state match {
      case s: DispenseState if(s.product == Product(50, "Peanut")) => true
      case _ => false
    })

    (new AddWarning(statePointCut, "Contains Nuts!"))(nfa)
  }
}
