package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class VendingMachineTest extends AnyFlatSpec with ChiselScalatestTester with FSMTest {

    val fsm = VendingMachine(VendingMachine.USCoinSet, 100, VendingMachine.GenericProducts)
    override val dfa = new DFA(fsm)

    it should "Accept 30c + Gum" in {
        testInput(List(Coin(5), Coin(25), Product(30, "Gum")), true)
        testInput(List(Coin(25), Coin(5), Product(30, "Gum")), true)
        testInput(List(Coin(10), Coin(10), Coin(10), Product(30, "Gum")), true)
        testInput(List(Coin(5), Coin(5), Coin(10), Coin(10), Product(30, "Gum")), true)
    }

    it should "Reject <30c + Gum" in {
        testInput(List(Product(30, "Gum")), false)
        testInput(List(Coin(5), Product(30, "Gum")), false)
        testInput(List(Coin(10), Product(30, "Gum")), false)
        testInput(List(Coin(25), Product(30, "Gum")), false)
        testInput(List(Coin(5), Coin(5), Product(30, "Gum")), false)
        testInput(List(Coin(5), Coin(10), Product(30, "Gum")), false)
        testInput(List(Coin(10), Coin(10), Product(30, "Gum")), false) 
    }

    it should "Accept >30c + Gum" in {
        testInput(List(Coin(5), Coin(25), Coin(5), Product(30, "Gum")), true)
        testInput(List(Coin(25), Coin(5), Coin(10), Product(30, "Gum")), true)
        testInput(List(Coin(10), Coin(10), Coin(10), Coin(25), Product(30, "Gum")), true)
        testInput(List(Coin(5), Coin(5), Coin(10), Coin(10), Coin(10), Product(30, "Gum")), true)
    }

}

