package foam.examples

import foam.examples.{NamedState, NumericToken}
import foam.product.Product
import foam.{Emitter, NFA, State}


object ProductMachine {

  // A machine that accepts an odd number of 1s in a binary input string.
  val stateEven: State = NamedState("Even", isAccept = false)
  val stateOdd: State = NamedState("Odd", isAccept = true)

  val oddNumberOf1sMachine: NFA = new NFA(stateEven)
    .addTransition((stateEven, NumericToken(0)), stateEven)
    .addTransition((stateEven, NumericToken(1)), stateOdd)
    .addTransition((stateOdd, NumericToken(0)), stateOdd)
    .addTransition((stateOdd, NumericToken(1)), stateEven)

  // A machine that accepts binary input strings that contain "000" as a substring.
  val stateSubstringReset: State = NamedState("Ø", isAccept = false)
  val stateSubstring0: State = NamedState("0", isAccept = false)
  val stateSubstring00: State = NamedState("00", isAccept = false)
  val stateSubstring000: State = NamedState("000", isAccept = true)

  val contains000SubstringMachine: NFA = new NFA(stateSubstringReset)
    .addTransition((stateSubstringReset, NumericToken(1)), stateSubstringReset)
    .addTransition((stateSubstringReset, NumericToken(0)), stateSubstring0)
    .addTransition((stateSubstring0, NumericToken(1)), stateSubstringReset)
    .addTransition((stateSubstring0, NumericToken(0)), stateSubstring00)
    .addTransition((stateSubstring00, NumericToken(1)), stateSubstringReset)
    .addTransition((stateSubstring00, NumericToken(0)), stateSubstring000)
    .addTransition((stateSubstring000, NumericToken(0)), stateSubstring000)
    .addTransition((stateSubstring000, NumericToken(1)), stateSubstring000)

  // This machine accepts binary input strings that contain an odd number of 1s and contain the substring 000.
  // If `isConjunctionMachine` were set to false, then it would accept binary input strings that contain either an odd
  // number of 1s or contain the substring 000.
  val productMachine: NFA = Product(oddNumberOf1sMachine, contains000SubstringMachine, isConjunctionMachine = true)

  Emitter.emitGV(productMachine)
}
