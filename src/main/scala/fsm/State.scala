package fsm

trait State {

    var transitions: Set[Transition];
    var body: List[Token => Unit];

    def executeCode(token: Token): Unit

    def getName(): String = { null }
}
