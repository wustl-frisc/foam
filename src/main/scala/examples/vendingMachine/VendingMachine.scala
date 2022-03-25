package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._
import fsm.featuredfsm.aspects._

object VendingMachine {
  def apply(): FeatureOrientedFSM = {
    val USCoinSet = Set[Coin](Coin(25), Coin(5), Coin(10))

    val BritishCoinSet = Set[Coin](Coin(5), Coin(10), Coin(20), Coin(50))

    val productSet = Set[Product](Product(60, "Soda"),
      Product(100, "Chips"),
      Product(30, "Gum"))

    val fsm = FeatureOrientedFSM(ValueState(0), SimpleStateFactory(), SimpleStateFactory())

    val coinFeatures = for(c <- USCoinSet) yield (new AddCoin(c, 100))
    val productFeatures = for(p <- productSet) yield (new AddProduct(p))
    val features = coinFeatures ++ productFeatures

    val finalFSM = applyHelper(features, fsm)

    Emitter(finalFSM)
    finalFSM
  }

  private def applyHelper(aspectSet: Set[FSMAspect], fsm: FeatureOrientedFSM): FeatureOrientedFSM = {
    val finalFSM = aspectSet.foldLeft(fsm)((newFSM, a) => a(newFSM))
    if(!finalFSM.equals(fsm)) applyHelper(aspectSet, finalFSM)
    else finalFSM
  }
}
