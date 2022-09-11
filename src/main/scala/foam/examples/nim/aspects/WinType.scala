package foam.examples.nim.aspects

import foam.{Lambda, MultiState, NFA, State}
import foam.aspects.{AfterState, Aspect, Pointcutter, StateJoinpoint}
import foam.examples.nim.states.{HeapState, PlayerState, WinState}

class WinType(isNormalPlay: Boolean) extends Aspect[NFA] {

  override def apply(base: NFA): NFA = {

    // Pointcut is of MultiStates that contain both a PlayerState & a HeapState, with every value within the HeapState
    // being the target value to "win" the heap.
    val statePointCut = Pointcutter[State, MultiState](base.states, {
      case ms: MultiState => ms.s.exists(_.isInstanceOf[PlayerState]) &&
        ms.s.exists({
          case hs: HeapState => hs.currentHeapState.zip(hs.heapScheme.map(_.target)).forall({
              case (currentValue: Int, target: Int) => currentValue == target
            })
          case _ => false
        })
      case _ => false
    })

    AfterState[MultiState](statePointCut, base)((thisJoinPoint: StateJoinpoint[MultiState], thisNFA: NFA) => {

      val playerState = thisJoinPoint.point.s.filter(_.isInstanceOf[PlayerState]).head.asInstanceOf[PlayerState]

      thisJoinPoint.out match {
        case Some(_) => (None, thisNFA)
        case None => (Some((Lambda, WinState(isNormalPlay, playerState.playerNum))), thisNFA)
      }
    })

  }
}
