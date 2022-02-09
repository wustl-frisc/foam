package `example-client`
import fsm._

class Tokenizer {

    def makeInitialFsm() = {
        val fsm = new FSM();
    }
}

object Main extends App {
    val x: Transition = new Transition(new Lambda(), new SimpleState());
    x.token = new Character('a');
    println(x.token.isLamda());
}