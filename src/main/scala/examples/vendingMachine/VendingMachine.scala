package examples

import featuredfsm._
import aspects._

object VendingMachine {
  def apply(): FeatureOrientedFSM = {
    val coinSet = Set[Coin](Coin(25), Coin(10), Coin(5))
    val productSet = Set[Product](Product(60, "Soda"),
      Product(100, "Chips"),
      Product(30, "Gum"))
    val fsm = FeatureOrientedFSM(ValueState(0), SimpleStateFactory(), SimpleStateFactory())
    val coinedFSM = coinSet.foldLeft(fsm)((newFSM, c) => { AddCoin(c, 100, newFSM) })
    val productedFSM = productSet.foldLeft(coinedFSM)((newFSM, p) => {AddProduct(p, newFSM)})
    // Emitter(productedFSM)
    productedFSM
  }
}
