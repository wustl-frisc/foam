package fsm

class SimpleState() extends State {

    var transitions = Set[Transition]();
    var body = List[Token => Unit]();

    override def executeCode(token: Token) = {
        for (f <- body) {
            f(token);
        }
    }

}
