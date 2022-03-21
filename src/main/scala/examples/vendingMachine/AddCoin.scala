package examples

import featuredfsm._
import fsm._
import aspects._

object AddCoin extends Aspect {
  def apply(coin: Coin, threshold: Int, fsm: FeatureOrientedFSM) = {

    //Add the token
    val tokenedFSM = fsm.addToken(coin)

    //Select only the ValueStates
    val statePointcut = Pointcut.byToken[ValueState](coin, tokenedFSM)

    //generate new states based on the values that exist
    val newStates = for ((s, _) <- statePointcut; v <- ((coin.value + s.total) to threshold by coin.value)) yield (ValueState(v))

    //add the new value states to the FSM
    val statedFSM = newStates.foldLeft(tokenedFSM)((newFSM, s) => newFSM.addState(s, s.total.toString))

    //Select all the keys for ValueStates that point to error
    val destinationPointcut = Pointcut.byDestination[ValueState](fsm.error, statedFSM)

    //set the correct destination for each
    val destinationedFSM = destinationPointcut.foldLeft(statedFSM)((newFSM, k) => {
      val state = k._1.asInstanceOf[ValueState]
      val token = k._2.asInstanceOf[Coin]

      if (state.total + token.value <= threshold) {
        val joinpoint = Set[(State, Token)](k)
        val destination = Set[State](newFSM.nameMap((state.total + token.value).toString))
        SetUnion(joinpoint, destination, newFSM)
      } else {
        newFSM
      }
    })

    destinationedFSM
  }
}
