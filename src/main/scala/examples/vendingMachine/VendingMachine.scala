package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._
import aspects._

object VendingMachine {
  val USCoinSet = Set[Coin](Coin(25), Coin(5), Coin(10))

  val BritishCoinSet = Set[Coin](Coin(5), Coin(10), Coin(20), Coin(50))

  val GenericProducts = Set[Product](
    Product(60, "Soda"),
    Product(100, "Chips"),
    Product(30, "Gum"),
    Product(50, "Peanut"))

  private val start = SimpleStateFactory(false)

  val namer: Any => String = (element) => element match {
    case s: State if(s == start) => "Start"
    case other => other.toString
  }

  def apply(coinSet: Set[Coin], threshold: Int, productSet: Set[Product]) = {

    val fsm = (new NFA(start)).addTransition((start, Lambda), ValueState(0, true))

    val coinFeatures = (for(c <- coinSet) yield (new AddCoin(c, threshold))).toList
    val productFeatures = (for(p <- productSet) yield (new DispenseProduct(p))).toList
    val features = coinFeatures ++ productFeatures :+ (new PeanutWarning) :+ (new PrintFunds) :+
      (new InsufficientFunds) :+ (new BuyMore) :+ (new ChangeReturn)

    val finalFSM = Weaver[NFA](features, fsm, (before: NFA, after: NFA) => before.isEqual(after))

    finalFSM
  }
}
