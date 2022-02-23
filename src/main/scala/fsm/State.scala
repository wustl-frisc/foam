package fsm

trait State {

    def body: List[Token => Unit]
    def body_=(l: List[Token => Unit]): Unit

    def executeCode(token: Token): Unit

}
