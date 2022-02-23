package `featuredfsm`
import fsm._

class SimpleState(val body: List[Token=>Unit] = List[Token => Unit]()) extends State {

    override def executeCode(token: Token) = {
        for (f <- body.reverseIterator) {
            f(token)
        }
    }

}
