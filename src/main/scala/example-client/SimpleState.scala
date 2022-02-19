package `example-client`
import fsm._

final case class SimpleState(val body: List[Token=>Unit] = List[Token => Unit]()) extends State {

    override def executeCode(token: Token) = {
        for (f <- body.reverseIterator) {
            f(token)
        }
    }

    def addCode(s: State, f: Token => Unit): SimpleState = {
        SimpleState(f :: body)
    }

}
