package edu.wustl.sbs
package fsm
package featuredfsm

private final case class SimpleState(val id: Int) extends State {

    override def executeCode = {
        CodeManager.signal(this)
    }

    override def toString(): String = {
        "SimpleState" + id
    }

}

object SimpleStateFactory {

    private var stateCount = 0;

    def apply(): State = {
        stateCount += 1
        SimpleState(stateCount)
    }
}
