package `featuredfsm`
import fsm._

private final case class SimpleState(private val id: Int) extends State {

    override def executeCode(token: Token) = {
        CodeManager.signal(this, token)
    }

}

class SimpleStateFactory() {

    private var stateCount = 0;

    def makeState(): State = {
        stateCount += 1
        SimpleState(stateCount)
    }


}
