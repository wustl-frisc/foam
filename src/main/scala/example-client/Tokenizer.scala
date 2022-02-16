package `example-client`
import fsm._

class Tokenizer {

    var fsm = new FeatureOrientedFSM()
    
}

object Main extends App {

    val numFinder = (new Tokenizer()).fsm
    println(numFinder.states)

    numFinder.addTransition(new Transition(numFinder.start, new Lambda(), numFinder.acceptState))

    println(numFinder.states)
    println(numFinder.accept(List[Token]()))

}