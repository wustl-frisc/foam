package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._
import fsm.featuredfsm.aspects._

class AddProduct (product: Product) extends FSMAspect {
  def apply(fsm: FeatureOrientedFSM) = {
    //Add the new product token
    val tokenedFSM = fsm.addToken(product)

    //Select all the value states
    val tokenPointcut = Pointcut.byToken[ValueState](product, tokenedFSM)

    //setup the new transitions
    val transitionedFSM = tokenPointcut.foldLeft(tokenedFSM)((newFSM, k) => {
      if (k._1.total >= product.value) { //if we have enough money, go to accept
        val joinpoint = Set[TransitionKey](k)
        val destination = Set[State](newFSM.acceptState)
        SetUnion(joinpoint, destination, newFSM)
      } else { //if we don't have enough money, try again
        val joinpoint = Set[TransitionKey](k)
        val destination = Set[State](k._1)
        SetUnion(joinpoint, destination, newFSM)
      }
    })

    transitionedFSM
  }
}
