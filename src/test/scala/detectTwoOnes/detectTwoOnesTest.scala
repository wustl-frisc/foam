package examples

import featuredfsm._
import fsm._

import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class DetectTwoOnesTest extends AnyFlatSpec with ChiselScalatestTester with FSMTest {

    val fsm = DetectTwoOnes.fsm

    it should "Reject 0" in {
        testString("0", false)
    }
    it should "Reject 1" in {
        testString("1", false)
    }
    it should "Reject 00" in {
        testString("00", false)
    }
    it should "Accept 11" in {
        testString("11", true)
    }
    it should "Reject 01" in {
        testString("01", false)
    }
    it should "Reject 10" in {
        testString("10", false)
    }
    it should "Accept 011" in {
        testString("011", true)
    }
    it should "Reject 101" in {
        testString("101", false)
    }
    it should "Reject 001" in {
        testString("001", false)
    }
    it should "Reject 010" in {
        testString("010", false)
    }
    it should "Accept 01010101011" in {
        testString("01010101011", true)
    }
    it should "Accept 100101001001011" in {
        testString("100101001001011", true)
    }
    it should "Reject 1010101010000101010" in {
        testString("1010101010000101010", false)
    }

    def testString(s: String, outcome: Boolean) = {
        implicit var inputList = List[Token]()
        for (ch <- s.toCharArray.reverse) {
            if (ch == '1') {
                inputList = One::inputList
            } else {
                inputList = Zero::inputList
            }
        }
        testInput(inputList, outcome)
    }

}
