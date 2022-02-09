package `example-client`
import fsm._

class Tokenizer {

    var fsm = new FeatureOrientedFSM()
    fsm.addState(new SimpleState())
    
}

object Main extends App {

    val numFinder = new Tokenizer()
    println(numFinder.fsm.states)


}