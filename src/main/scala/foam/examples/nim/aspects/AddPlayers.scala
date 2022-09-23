package foam.examples.nim.aspects

import foam.{NFA, State}
import foam.aspects.{AroundState, Aspect, Pointcutter, StateJoinpoint}
import foam.examples.nim.states.{PlayerState, StartState}
import foam.examples.nim.tokens.ChangePlayerToken

class AddPlayers(numPlayers: Int) extends Aspect[NFA] {

  override def apply(base: NFA): NFA = {
    val statePointCut = Pointcutter[State, State](base.states, {
      case _: PlayerState => true
      case _: StartState => true
      case _ => false
    })

    AroundState[State](statePointCut, base)((thisJointPoint: StateJoinpoint[State], thisNFA: NFA) => {
      val nextPlayer: Int = thisJointPoint.point match {
        case s: PlayerState => (s.playerNum + 1) % numPlayers
        case _ => 0 // Covers StartState
      }

      val newNFA: NFA = thisNFA.addTransition((thisJointPoint.point, ChangePlayerToken(nextPlayer)), PlayerState(nextPlayer))
      (thisJointPoint.point, newNFA)
    })

  }

}
