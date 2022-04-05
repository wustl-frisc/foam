package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._
import aspects._

object VendingMachine {
  val USCoinSet = Set[Coin](Coin(25), Coin(5), Coin(10))

  val BritishCoinSet = Set[Coin](Coin(5), Coin(10), Coin(20), Coin(50))

  val GenericProducts = Set[Product](Product(60, "Soda"), Product(100, "Chips"),Product(30, "Gum"))

  def apply(coinSet: Set[Coin], threshold: Int, productSet: Set[Product]) = {

    val start = ValueState(0)
    val accept = SimpleStateFactory()
    val error = SimpleStateFactory()

    val nameMap = Map[State, String](start -> "start", accept -> "accept", error -> "error")

    val fsm = new NFA(start, accept, error)

    val coinFeatures = for(c <- coinSet) yield (new AddCoin(c, threshold))
    val dispenseFeatures = for(p <- productSet) yield (new DispenseProduct(p))
    val features = coinFeatures ++ dispenseFeatures +
      (new MakeChange) +
      (new ChangeReturn) +
      (new TryAgain) +
      (new FundsWarning)

    val finalFSM = Weaver[NFA](features, fsm, (before: NFA, after: NFA) => before.isEqual(after))

    Emitter(finalFSM, element => element match {
      case state: State if (nameMap contains state) => nameMap(state)
      case state: State => state.toString
      case token: Token => token.toString
    }, true)

    finalFSM
  }
}
