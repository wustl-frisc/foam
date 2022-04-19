package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

trait FSMTest extends AnyFlatSpec with ChiselScalatestTester {

    def dfa: DFA

    def testInput(input: List[Token], outcome: Boolean) = {
        test(new ChiselFSM(dfa)) { dut => {
            val tokenMap = dut.tokenMap
            for (token <- input) {
                dut.io.in.poke(tokenMap(token).U)
                dut.clock.step()
            }
            dut.io.out.expect(outcome.B)
        }}
    }

}
