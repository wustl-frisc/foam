
package foam

class NFA private (override val start: State,
  override val states: Set[State],
  override val alphabet: Set[Token],
  val transitions: Map[TransitionKey, Set[State]]) extends FSM {

  def this(start: State) = {
    this(start, Set[State](start), Set[Token](Lambda), Map[TransitionKey, Set[State]]((start, Lambda) -> Set[State]()))
  }

  def +(otherNFA: NFA): NFA = {
    new NFA(start, this.states ++ otherNFA.states, this.alphabet ++ otherNFA.alphabet, this.transitions ++ otherNFA.transitions)
  }

  def addTransition(k: TransitionKey, d: State): NFA = {
    val newFSM = this.addState(k._1).addToken(k._2).addState(d)
    new NFA(start, newFSM.states, newFSM.alphabet, newFSM.transitions + (k -> (newFSM.transitions(k) + d)))
  }

  def removeTransition(k: TransitionKey, d: State): NFA = {
    val newFSM = this.addState(k._1).addToken(k._2).addState(d)
    new NFA(start, newFSM.states, newFSM.alphabet, newFSM.transitions + (k -> (newFSM.transitions(k) - d)))
  }

  def removeState(d: State): NFA = {
    val newTransitions = transitions.filter(_._1._1 match {
      case s: State if s == d => false
      case _ => true
    })

    new NFA(start, states - d, alphabet, newTransitions)
  }

  def clearTransitions(k: TransitionKey): NFA = {
    val newFSM = this.addState(k._1).addToken(k._2)
    new NFA(start, newFSM.states, newFSM.alphabet, newFSM.transitions + (k -> Set[State]()))
  }

  private def addToken(t: Token) = if (alphabet contains t) {
    this
  } else {
    //construct a new transition for each existing state to the error state on this token
    val newTransitions = for (s <- states) yield (s, t) -> Set[State]()

    new NFA(start, states, alphabet + t, transitions ++ newTransitions.toMap)
  }

  private def addState(s: State) = if (states contains s) {
    this
  } else {

    //construct a new transition for each token from this state to the error state
    val newTransitions = for (t <- alphabet) yield (s, t) -> Set[State]()

    new NFA(start, states + s, alphabet, transitions ++ newTransitions.toMap)
  }

  private def executeHelper(input: List[Token], s: State): Set[State] = {
    val token = if (input.nonEmpty) input.head else Lambda
    var finalStates = Set[State]()

    if (input.isEmpty && s.isAccept) {
      finalStates + s
    } else {
      for (t <- alphabet) {
        for (d <- transitions((s, t))) {
          if (t.isLamda) {
            finalStates = finalStates ++ executeHelper(input, d)
          } else if (t == token) {
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
    val finalStates = executeHelper(input, start)
    if (finalStates.foldLeft(false)((prevIsAccept, state) => prevIsAccept || state.isAccept)) println("Execution Sucess!")
    else println("Execution failed")
    finalStates
  }
}
