package examples

import featuredfsm._
import fsm._
import aspects._

object AddCoin extends Aspect {
  def apply(coin: Coin, threshold: Int, fsm: FeatureOrientedFSM) = {
    val tokenedFSM = fsm.addToken(coin)

    val statePointcut = Pointcut.byToken[ValueState](coin, tokenedFSM)

    val newStates = for ((s, _) <- statePointcut; v <- ((coin.value + s.total) to threshold by coin.value)) yield (ValueState(v))

    val statedFSM = newStates.foldLeft(tokenedFSM)((newFSM, s) => newFSM.addState(s, s.total.toString))

    val tokenPointcut = Pointcut.byToken[ValueState](coin, statedFSM)

    val transitionedFSM = tokenPointcut.foldLeft(statedFSM)((newFSM, k) => {
      if (k._1.total + coin.value <= threshold) {
        val joinpoint = Set[(State, Token)](k)
        val destination = Set[State](newFSM.nameMap((k._1.total + coin.value).toString))
        SetUnion(joinpoint, destination, newFSM)
      } else {
        newFSM
      }
    })

    transitionedFSM
  }
}
