
package foam

case class MultiState(s: Iterable[State], isAccept: Boolean) extends State {

  override def toString: String = s.map(_.toString).reduceLeft(_ + " and " + _)

}

// Ensure that MultiStates all have more than one state and are not recursive
object MultiStateFactory {

  /**
   * Combines a collection of states into a single MultiState
   *
   * @param s an iterable collection of states
   * @param isAcceptingConjunction if true, then every state in `s` must be an accepting state for the MultiState to
   *                               also be accepting. If false, then it only takes one.
   * @return the combined state.
   */
  def apply(s: Iterable[State], isAcceptingConjunction: Boolean = false): State = {

    if (s.size == 1) {
      s.toList.head
    } else if (s.size > 1) {
      var cumulativeSeq = Seq[State]()
      for (state: State <- s) {
        state match {
          case MultiState(states, _) => cumulativeSeq = cumulativeSeq ++ states
          case anyOther => cumulativeSeq = cumulativeSeq :+ anyOther
        }
      }

      // If we are taking the conjunction of isAccepting across states, check that all states are an accepting state.
      // Otherwise, we are taking the disjunction => check if any state is an accepting state.
      val isAccept = if(isAcceptingConjunction) cumulativeSeq.forall(_.isAccept) else cumulativeSeq.exists(_.isAccept)

      MultiState(cumulativeSeq, isAccept)
    } else {
      throw new IllegalArgumentException("Can't create a MultiState with an empty set")
    }
  }
}
