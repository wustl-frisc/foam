package edu.wustl.sbs
package fsm

class NFA private (start: State,
  val acceptState: State,
  error: State,
  states: Set[State],
  alphabet: Set[Token],
  val transitions: Map[TransitionKey, Set[State]]) extends FSM {

  override def accept = Set[State](acceptState)

  def this(start: State, acceptState: State, error: State) = {
    this(start, acceptState, error, Set[State](start, acceptState, error), Set[Token](Lambda), Map[TransitionKey, Set[State]]((start, Lambda) -> Set[State](), (error, Lambda) -> Set[State](), (acceptState, Lambda) -> Set[State]()))
  }

  def addTransition(k: TransitionKey, d: State) = {
    val newFSM = addState(k._1).addToken(k._2).addState(d)

    new NFA(start, acceptState, error, newFSM.states, newFSM.alphabet,
      if(transitions(k) contains error) transitions + (k -> (transitions(k) - error + d)) else transitions + (k -> (transitions(k) + d)))
  }

  private def addToken(t: Token) = if (alphabet contains t) {
    this
  } else {
    //construct a new transition for each existing state to the error state on this token
    val newTransitions = (for (s <- states) yield ((s, t) -> Set[State](error)))

    new NFA(start, acceptState, error, states, alphabet + t, transitions ++ newTransitions.toMap)
  }

  private def addState(s: State) = if (states contains s) {
    this
  } else {

    //construct a new transition for each token from this state to the error state
    val newTransitions = (for (t <- alphabet) yield (
      if (t != Lambda) {
        (s, t) -> Set[State](error)
      } else {
        (s, t) -> Set[State]()
      }))

    new NFA(start, acceptState, error, states + s, alphabet, transitions ++ newTransitions.toMap)
  }

  private def executeHelper(input: List[Token], s: State): Set[State] = {
    val token = if (input.length > 0) input.head else Lambda
    var finalStates = Set[State]()

    if (input.length == 0 && s == acceptState) {
      finalStates + s
    } else {
      for (t <- alphabet) {
        for (d <- transitions((s, t))) {
          if (t.isLamda) {
            d.executeCode
            finalStates = finalStates ++ executeHelper(input, d)
          } else if (t == token) {
            d.executeCode
            finalStates = finalStates ++ executeHelper(input.tail, d)
          }
        }
      }
      if (finalStates == Set[State]()) {
        finalStates += s
      }
      finalStates
    }
  }

  def execute(input: List[Token]): Set[State] = {
    start.executeCode
    val finalStates = executeHelper(input, start)
    if (finalStates contains acceptState) println("Execution Sucess!")
    else println("Execution failed")
    finalStates
  }
}
