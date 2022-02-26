package `scalaclient`
import fsm._
import featuredfsm._

// Currently accpts lists of digits, lowercase letters
object Tokenizer {

    var token: String = ""

    val factory = new SimpleStateFactory();

    val start = factory.makeState()
    val acceptState = factory.makeState()
    var fsm = FeatureOrientedFSM.initiliaze(start, acceptState)

    val zero = factory.makeState()
    val hex = factory.makeState()
    val number = factory.makeState()
    val variable = factory.makeState()
    val normalStates = Set[State](zero, hex, number, variable)
    for (state <- normalStates) {
        CodeManager.addCode(state, (x) => {
            token += (
                if (x == Lambda) "" else x
            )
        })
    }
    CodeManager.addCode(start, (x) => {             // Adding code to start state
        token = ""
    })

    val acceptNumber = factory.makeState()
    val acceptHex = factory.makeState()
    val acceptVariable = factory.makeState()
    val preAcceptStates = Set[State](acceptNumber, acceptHex, acceptVariable)
    fsm = fsm.addTransition(Transition(zero, Character(' '), acceptNumber))
    fsm = fsm.addTransition(Transition(number, Character(' '), acceptNumber))
    fsm = fsm.addTransition(Transition(hex, Character(' '), acceptHex))
    fsm = fsm.addTransition(Transition(variable, Character(' '), acceptVariable))
    for (state <- preAcceptStates) {
        fsm = fsm.addTransition(Transition(state, Lambda, fsm.acceptState))
    }
    CodeManager.addCode(acceptNumber, (x) => {println("Found a number value: " + token.toInt)})
    CodeManager.addCode(acceptHex, (x) => {println("Found a hexadecimal value: " + token)})
    CodeManager.addCode(acceptVariable, (x) => {println("Found a variable: " + token)})



    // From start
    fsm = fsm.addTransition(Transition(fsm.start, Character('0'), zero))
    for (i <- 0 to 8) {
        fsm = fsm.addTransition((Transition(fsm.start, Character((49+i).toChar), number)))      // '1' to '9'
    }
    for (i <- 0 to 25) {
        fsm = fsm.addTransition((Transition(fsm.start, Character((97+i).toChar), variable)))    // 'a' to 'z'
    }

    // From zero
    fsm = fsm.addTransition(Transition(zero, Character('X'), hex))
    fsm = fsm.addTransition(Transition(zero, Character('x'), hex))
    for (i <- 0 to 9) {
        fsm = fsm.addTransition(Transition(zero, Character((48+i).toChar), number))             // '0' to '9'
    }

    // From hex
    for (i <- 0 to 9) {
        fsm = fsm.addTransition(Transition(hex, Character((48+i).toChar), hex))                 // '0' to '9'
    }
    for (i <- 0 to 5) { 
        fsm = fsm.addTransition(Transition(hex, Character((65+i).toChar), hex))                 // 'A' to 'F'
    }

    // From number
    for (i <- 0 to 9) {
        fsm = fsm.addTransition(Transition(number, Character((48+i).toChar), number))           // '0' to '9'
    }

    // From variable
    for (i <- 0 to 9) {
        fsm = fsm.addTransition(Transition(variable, Character((48+i).toChar), variable))       // '0' to '9'
    }
    for (i <- 0 to 25) {
        fsm = fsm.addTransition((Transition(variable, Character((97+i).toChar), variable)))     // 'a' to 'z'
    }

}