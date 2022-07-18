package foam.examples.crossProduct

import foam.examples.{NamedState, NumericToken, TextToken}
import foam.product.CrossProduct
import foam.{Emitter, NFA, State}

object Nim {

  val includeLoopFrom0To3: Boolean = false

  // A heap state machine -- the heap states allow for 3, 2, 1, and 0 tokens to be remaining.
  val state3: State = NamedState("3", isAccept = false)
  val state2: State = NamedState("2", isAccept = false)
  val state1: State = NamedState("1", isAccept = false)
  val state0: State = NamedState("0", isAccept = true)

  // Each turn, either 1 or 2 sticks are removed from the heap, with the 0 state being accepting.
  // If includeLoopFrom0To3 is true, then there's an additional transition from the 0 state to the 3 state.
  var heapStateMachine: NFA = new NFA(state3)
    .addTransition((state3, NumericToken(-1)), state2)
    .addTransition((state3, NumericToken(-2)), state1)
    .addTransition((state2, NumericToken(-1)), state1)
    .addTransition((state2, NumericToken(-2)), state0)
    .addTransition((state1, NumericToken(-1)), state0)

  if (includeLoopFrom0To3) {
    heapStateMachine = heapStateMachine.addTransition((state0, NumericToken(-1)), state3)
  }

  // A player machine -- it alternates between players A and B on the tokens "b" (A -> B) and "a" (B -> A).
  // Both players are accepting.
  val stateA: State = NamedState("A", isAccept = true)
  val stateB: State = NamedState("B", isAccept = true)

  val playerMachine: NFA = new NFA(stateA)
    .addTransition((stateA, TextToken("b")), stateB)
    .addTransition((stateB, TextToken("a")), stateA)

  // This machine represents the subtraction game of Nim, starting with 3 sticks and having two players alternate turns.
  val gameOfNim: NFA = CrossProduct(heapStateMachine, playerMachine)
  Emitter.emitGV(gameOfNim, _.toString)
}
