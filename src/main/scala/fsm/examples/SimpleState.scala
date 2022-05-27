package edu.wustl.sbs
package fsm
package examples

private final case class SimpleState(val id: Int, val isAccept: Boolean) extends State {

    override def toString(): String = {
        "SimpleState" + id
    }

}

object SimpleStateFactory {

    private var stateCount = 0;

    def apply(isAccept: Boolean): State = {
        stateCount += 1
        SimpleState(stateCount, isAccept)
    }
}
