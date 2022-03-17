package featuredfsm
import fsm._

final case class FeatureOrientedFSM private (
  val start: State,
  val acceptState: State,
  val error: State,
  val states: Set[State],
  val alphabet: Set[Token],
  val transitions: Map[(State, Token), Set[State]],
  val nameMap: Map[String, State]) extends FSM {

  override def accept: Set[State] = Set[State](acceptState)

  def addToken(t: Token) = if (alphabet contains t) {
    this
  } else {
    //construct a new transition for each existing state to the error state on this token
    val newTransitions = (for (s <- states) yield ((s, t) -> Set[State](error)))

    FeatureOrientedFSM(start, acceptState, error, states, alphabet + t, transitions ++ newTransitions.toMap, nameMap)
  }

  def addState(s: State, name: String) = if ((states contains s) || (nameMap contains name)) {
    this
  } else {

    //construct a new transition for each token from this state to the error state
    val newTransitions = (for (t <- alphabet) yield (
      if (t != Lambda) {
        (s, t) -> Set[State](error)
      } else {
        (s, t) -> Set[State]()
      }))

    FeatureOrientedFSM(start, acceptState, error, states + s, alphabet, transitions ++ newTransitions.toMap, nameMap + (name -> s))
  }

  def addTransition(k: (State, Token), d: State) = if (((alphabet + Lambda) contains k._2) &&
    (states contains k._1) && (states contains d)) {

    if (transitions(k) contains error) { //remove the error state if it exists
      FeatureOrientedFSM(start, acceptState, error, states, alphabet, transitions + (k -> (transitions(k) - error + d)), nameMap)
    } else { //if there's already
      FeatureOrientedFSM(start, acceptState, error, states, alphabet, transitions + (k -> (transitions(k) + d)), nameMap)
    }
  } else {
    this
  }

  def removeTransition(k: (State, Token), d: State) = if (!(transitions contains k)) {
    this
  } else {
    //if removing the state empties the destination list, then put the error state back in
    if ((transitions(k) - d).isEmpty && k._2 != Lambda) { //allow lambda to go to empty set
      FeatureOrientedFSM(start, acceptState, error, states, alphabet, transitions + (k -> Set[State](error)), nameMap)
    } else { //just take out the state
      FeatureOrientedFSM(start, acceptState, error, states, alphabet, transitions + (k -> (transitions(k) - d)), nameMap)
    }
  }

  /* def insertFsm(s: State, fsm: FeatureOrientedFSM) = {
        // Add s to machine if needed
        var newStates = states + s

        // Move the former transitions of s to fsm.accept
        var newTransitions = Set[Transition]()
        for (transition <- transitions) {
            if (transition.source == s) {
                newTransitions += Transition(fsm.acceptState, transition.token, transition.destination)
            } else {
                newTransitions += transition
            }
        }

        // Give s a single transition to fsm.start
        newTransitions += Transition(s, Lambda, fsm.start)

        // Add all states and tokens from fsm to this
        newStates = newStates ++ fsm.states
        val newAlphabet = alphabet ++ fsm.alphabet

        FeatureOrientedFSM(start, acceptState, error, newStates, newAlphabet, newTransitions)
    } */

  // For purely example
  def execute(input: List[Token]): Set[State] = {
    start.executeCode(Lambda)
    val finalStates = executeHelper(input, start)
    if (finalStates contains acceptState) println("Execution Sucess!")
    finalStates
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
            d.executeCode(Lambda)
            finalStates = finalStates ++ executeHelper(input, d)
          } else if (t == token) {
            d.executeCode(token)
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
}

object FeatureOrientedFSM {
  def apply(start: State, accept: State, error: State): FeatureOrientedFSM = {
    FeatureOrientedFSM(
      start,
      accept,
      error,
      Set[State](accept, start, error),
      Set[Token](Lambda),
      Map[(State, Token), Set[State]]((start, Lambda) -> Set[State](), (accept, Lambda) -> Set[State]()),
      Map[String, State]("start" -> start, "accept" -> accept, "error" -> error))
  }
}
