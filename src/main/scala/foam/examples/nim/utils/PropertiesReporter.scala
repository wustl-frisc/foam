package foam.examples.nim.utils

import foam.NFA

object PropertiesReporter {

  private def report(name: String, nfa: NFA): Unit = {
    println(s"$name:\t${nfa.states.size} states,\t${nfa.alphabet.size} tokens,\t${nfa.transitions.size} transitions")
  }

  def apply(heapFSM: NFA, playerFSM: NFA, nimFSM: NFA, nimFSMWithWinner: NFA): Unit = {
    report("Heap", heapFSM)
    report("Player", playerFSM)
    report("Nim", nimFSM)
    report("NimWithWinners", nimFSMWithWinner)
  }

}
