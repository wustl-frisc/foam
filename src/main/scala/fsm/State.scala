package fsm

trait State {

    def body: List[Token => Unit]

    def executeCode(token: Token): Unit

}
