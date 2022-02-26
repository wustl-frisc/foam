package fsm

trait State {

    def executeCode(token: Token): Unit

}
