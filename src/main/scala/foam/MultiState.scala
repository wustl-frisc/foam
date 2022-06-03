
package foam

case class MultiState(s: Set[State], val isAccept: Boolean) extends State {

    override def toString: String = {
        val strSet = s.map(state => state.toString())
        val sortedStrList = strSet.toList.sortWith(_.compareTo(_) < 0)
        sortedStrList.reduceLeft(_ + " and " + _)
    }

}

// Ensure that MultiStates all have more than one state and are not recursive
object MultiStateFactory {

    def apply(s: Set[State]): State = {
        if (s.size == 1) {
            s.toList(0)
        }
        else if (s.size > 1) {
            var cumulativeSet = Set[State]()
            for (state: State <- s) {
                state match {
                    case MultiState(set, _) => cumulativeSet = cumulativeSet ++ set
                    case anyOther => cumulativeSet = cumulativeSet + anyOther
                }
            }

            //if any of these are accept states, we need to count the whole thing
            var isAccept = cumulativeSet.foldLeft(false)((prevAccept, state) => prevAccept || state.isAccept)

            MultiState(cumulativeSet, isAccept)
        }
        else {
            throw new Exception("Can't create a MultiState with an empty set")
        }
    }
}
