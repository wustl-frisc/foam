package `scalaclient`
import fsm._
import featuredfsm._

object Tokenizer {

    // def construct(fsm: FeatureOrientedFSM, list: List[Unit => FeatureOrientedFSM]) = {
    //     var x = fsm
    //     for (f <- list) {
    //         x = f(x)
    //     }
    //     x
    // }

    var fsm = FeatureOrientedFSM.initiliaze()
    fsm = fsm.addCode(fsm.start, (x) => {println("Start")})
    fsm = fsm.addCode(fsm.acceptState, (x) => {println("Accept")})
    fsm = fsm.addTransition(new Transition(fsm.start, new Lambda(), fsm.acceptState))

}

