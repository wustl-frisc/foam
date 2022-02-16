package `example-client`
import fsm._

class SimpleState() extends State {

    var transitions = Set[Transition]()
    var body = List[Token => Unit]()

    override def executeCode(token: Token) = {
        for (f <- body.reverseIterator) {
            f(token)
        }
    }

    def moveTransition(t: Transition, dest: State) = {
        val updated = Transition(t.source, t.token, dest)
        transitions -= t
        transitions += updated
    }

    def addCode(s: State, f: Token => Unit) = {
        f :: body
    }

    def changeToken(t: Transition, token: Token) = {
        val updated = Transition(t.source, token, t.destination)
        transitions -= t
        transitions += updated
    }

}
