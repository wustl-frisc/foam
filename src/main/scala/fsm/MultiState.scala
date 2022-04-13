package edu.wustl.sbs
package fsm

case class MultiState(s: Set[State]) extends State {

    override def executeCode: Unit = {
        for (state <- s) {
            state.executeCode
        }
    }

    override def toString: String = {
        val strSet = s.map(state => state.toString())
        val sortedStrList = strSet.toList.sortWith(_.compareTo(_) < 0)
        sortedStrList.reduceLeft(_ + " and\n " + _)
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
                    case MultiState(set) => cumulativeSet = cumulativeSet ++ set
                    case anyOther => cumulativeSet = cumulativeSet + anyOther
                }
            }
            MultiState(cumulativeSet)
        }
        else {
            throw new Exception("Can't create a MultiState with an empty set")
        }
    }
}