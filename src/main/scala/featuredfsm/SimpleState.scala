package `featuredfsm`
import fsm._

private final case class SimpleState(private val id: Int, private val name: String) extends State {

    override def executeCode(token: Token) = {
        CodeManager.signal(this, token)
    }

}

object SimpleStateFactory {

    private var stateCount = 0;

    def apply(name: String): State = {
        stateCount += 1
        SimpleState(stateCount, name)
    }


}
