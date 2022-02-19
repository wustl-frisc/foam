package `example-client`
import fsm._

object Tokenizer {

    var fsm = FeatureOrientedFSM.initiliaze()
    
}

object Main extends App {

    val numFinder = Tokenizer.fsm
    println(numFinder.states)

    numFinder.addTransition(new Transition(numFinder.start, new Lambda(), numFinder.acceptState))

    println(numFinder.states)
    println(numFinder.accept(List[Token]()))

}