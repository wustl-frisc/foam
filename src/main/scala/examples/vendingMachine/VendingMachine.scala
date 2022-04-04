package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._
import aspects._

object VendingMachine {
  val USCoinSet = Set[Coin](Coin(25), Coin(5), Coin(10))

  val BritishCoinSet = Set[Coin](Coin(5), Coin(10), Coin(20), Coin(50))

  def apply(coinSet: Set[Coin], threshold: Int) = {

    val start = ValueState(0)
    val accept = SimpleStateFactory()
    val error = SimpleStateFactory()

    val nameMap = Map[State, String](start -> "start", accept -> "accept", error -> "error")

    val fsm = new NFA(start, accept, error)

    val coinFeatures = for(c <- coinSet) yield (new AddCoin(c, threshold))
    //val productFeatures = for(p <- productSet) yield (new AddProduct(p))
    val features = coinFeatures //++ productFeatures

    val finalFSM = Weaver[NFA](features, fsm, (before: NFA, after: NFA) => before.isEqual(after))

    Emitter(finalFSM, element => element match {
      case state: State if (nameMap contains state) => nameMap(state)
      case state: ValueState => state.toString
      case token: Coin => token.toString
    })

    finalFSM
  }
}
