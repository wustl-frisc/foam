
package foam
package examples

import aspects._

object VendingMachine {
  val USCoinSet = Set[Coin](Coin(5), Coin(10), Coin(25))

  val BritishCoinSet = Set[Coin](Coin(5), Coin(10), Coin(20), Coin(50))

  val GenericProducts = Set[Product](
    Product(25, "Soda"),
    Product(50, "Gum"),
    Product(75, "Peanut"),
    Product(100, "Chips"))

  private val start = SimpleStateFactory(false)

  val namer: Any => String = (element) => element match {
    case s: State if(s == start) => "Start"
    case other => other.toString
  }

  def apply(coinSet: Set[Coin], threshold: Int, productSet: Set[Product], features: List[Aspect[NFA]]) = {

    val fsm = (new NFA(start)).addTransition((start, Lambda), TotalState(0, true))
    val coinFeatures = (for(c <- coinSet) yield (new AddCoin(c, threshold))).toList
    val productFeatures = (for(p <- productSet) yield (new DispenseProduct(p))).toList

    val finalFSM = Weaver[NFA](coinFeatures ++ productFeatures ++ features, fsm, (before: NFA, after: NFA) => before.isEqual(after))
    finalFSM
  }
}
