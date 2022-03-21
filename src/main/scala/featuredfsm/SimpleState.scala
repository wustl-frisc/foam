package featuredfsm
import fsm._

private final case class SimpleState(val id: Int) extends State {

    override def executeCode(token: Token) = {
        CodeManager.signal(this, token)
    }

}

object SimpleStateFactory {

    private var stateCount = 0;

    def apply(): State = {
        stateCount += 1
        SimpleState(stateCount)
    }
}
