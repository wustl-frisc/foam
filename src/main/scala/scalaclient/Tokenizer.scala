package `scalaclient`
import fsm._
import featuredfsm._

// Currently accpts lists of digits, lowercase letters
object Tokenizer {

    var token: String = "Value: "

    var fsm = FeatureOrientedFSM.initiliaze()
    // fsm = fsm.addCode(fsm.start, (x) => {println("Start")})
    // fsm = fsm.addCode(fsm.acceptState, (x) => {println("Accept")})

    // THIS BREAKS SCALA ACCEPT() METHOD
    // fsm = fsm.addTransition(new Transition(fsm.start, Lambda, fsm.acceptState))

    val zero = new SimpleState()
    val hex = new SimpleState()
    val number = new SimpleState()
    val variable = new SimpleState()
    val normalStates = Set[State](fsm.start, zero, hex, number, variable)
    for (state <- normalStates) {
        fsm = fsm.addCode(state, (x) => {
            token += (
                if (x == Lambda) "" else x
            )
        })
    }
    fsm = fsm.addCode(fsm.start, (x) => {
        token = "Value: "
    })
    fsm = fsm.addCode(fsm.acceptState, (x) => {
        println(token)
    })

    val acceptNumber = new SimpleState()
    val acceptHex = new SimpleState()
    val acceptVariable = new SimpleState()
    val preAcceptStates = Set[State](acceptNumber, acceptHex, acceptVariable)
    fsm = fsm.addTransition(Transition(zero, Character(' '), acceptNumber))
    fsm = fsm.addTransition(Transition(number, Character(' '), acceptNumber))
    fsm = fsm.addTransition(Transition(hex, Character(' '), acceptHex))
    fsm = fsm.addTransition(Transition(variable, Character(' '), acceptVariable))
    for (state <- preAcceptStates) {
        fsm = fsm.addTransition(Transition(state, Lambda, fsm.acceptState))
    }
    fsm = fsm.addCode(acceptNumber, (x) => {println("Found a number value")})
    fsm = fsm.addCode(acceptHex, (x) => {println("Found a hexadecimal value")})
    fsm = fsm.addCode(acceptVariable, (x) => {println("Found a variable")})



    // From start
    fsm = fsm.addTransition(Transition(fsm.start, Character('0'), zero))
    for (i <- 0 to 8) {
        fsm = fsm.addTransition((Transition(fsm.start, Character((49+i).toChar), number)))      // '1' + i
    }
    for (i <- 0 to 25) {
        fsm = fsm.addTransition((Transition(fsm.start, Character((97+i).toChar), variable)))    // 'a' + i
    }

    // From zero
    fsm = fsm.addTransition(Transition(zero, Character('X'), hex))
    fsm = fsm.addTransition(Transition(zero, Character('x'), hex))
    for (i <- 0 to 9) {
        fsm = fsm.addTransition(Transition(zero, Character((48+i).toChar), number))             // '0' + i
    }

    // From hex
    for (i <- 0 to 9) {
        fsm = fsm.addTransition(Transition(hex, Character((48+i).toChar), hex))                 // '0' + i
    }
    for (i <- 0 to 5) { 
        fsm = fsm.addTransition(Transition(hex, Character((65+i).toChar), hex))                 // 'A' + i
    }

    // From number
    for (i <- 0 to 9) {
        fsm = fsm.addTransition(Transition(number, Character((48+i).toChar), number))           // '0' + i
    }

    // From variable
    for (i <- 0 to 9) {
        fsm = fsm.addTransition(Transition(variable, Character((48+i).toChar), variable))       // '0' + i
    }
    for (i <- 0 to 25) {
        fsm = fsm.addTransition((Transition(variable, Character((97+i).toChar), variable)))     // 'a' + i
    }

}























    // def construct(fsm: FeatureOrientedFSM, list: List[Unit => FeatureOrientedFSM]) = {
    //     var x = fsm
    //     for (f <- list) {
    //         x = f(x)
    //     }
    //     x
    // }