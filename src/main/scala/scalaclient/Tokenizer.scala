package `scalaclient`
import fsm._
import featuredfsm._

// Currently accpts lists of digits, lowercase letters
object Tokenizer {

    var token: String = ""

    val start = SimpleStateFactory("start")
    val acceptState = SimpleStateFactory("accept")
    val error = SimpleStateFactory("error")
    var fsm = FeatureOrientedFSM(start, acceptState, error)

    val zero = SimpleStateFactory("zero")
    fsm = fsm.addState(zero)

    val hex = SimpleStateFactory("hex")
    fsm = fsm.addState(hex)

    val number = SimpleStateFactory("number")
    fsm = fsm.addState(number)

    val variable = SimpleStateFactory("variable")
    fsm = fsm.addState(variable)

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

    val acceptNumber = SimpleStateFactory("acceptNumber")
    fsm = fsm.addState(acceptNumber)

    val acceptHex = SimpleStateFactory("acceptHex")
    fsm = fsm.addState(acceptHex)

    val acceptVariable = SimpleStateFactory("acceptVariable")
    fsm = fsm.addState(acceptVariable)

    val spaceToken = Character(' ')
    fsm = fsm.addToken(spaceToken)
    val preAcceptStates = Set[State](acceptNumber, acceptHex, acceptVariable)
    fsm = fsm.removeTransition(Transition(zero, spaceToken, fsm.error))
    fsm = fsm.addTransition(Transition(zero, spaceToken, acceptNumber))

    fsm = fsm.removeTransition(Transition(number, spaceToken, fsm.error))
    fsm = fsm.addTransition(Transition(number, spaceToken, acceptNumber))

    fsm = fsm.removeTransition(Transition(hex, spaceToken, fsm.error))
    fsm = fsm.addTransition(Transition(hex, spaceToken, acceptHex))

    fsm = fsm.removeTransition(Transition(variable, spaceToken, fsm.error))
    fsm = fsm.addTransition(Transition(variable, spaceToken, acceptVariable))
    for (state <- preAcceptStates) {
        fsm = fsm.removeTransition(Transition(state, Lambda, fsm.error))
        fsm = fsm.addTransition(Transition(state, Lambda, fsm.acceptState))
    }
    CodeManager.addCode(acceptNumber, (x) => {println("Found a number value: " + token.toInt)})
    CodeManager.addCode(acceptHex, (x) => {println("Found a hexadecimal value: " + token)})
    CodeManager.addCode(acceptVariable, (x) => {println("Found a variable: " + token)})

    val zeroToken = Character('0')
    val capitalCaseXToken = Character('X')
    val lowerCaseXToken = Character('x')
    val oneToNineTokens = for(i <- 0 to 8) yield (Character((49+i).toChar))
    val zeroToNineTokens = Set(zeroToken) ++ oneToNineTokens
    val aToZTokens = for(i <- 0 to 25) yield (Character((97+i).toChar))
    val hexTokens = for(i <- 0 to 5) yield (Character((65+i).toChar))
    val allTokens = Set(zeroToken) ++ Set(capitalCaseXToken) ++ Set(lowerCaseXToken) ++ oneToNineTokens ++ aToZTokens ++ hexTokens
    allTokens foreach (t => {
      fsm = fsm.addToken(t)
    })

    // From start
    fsm = fsm.removeTransition(Transition(zero, zeroToken, fsm.error))
    fsm = fsm.addTransition(Transition(fsm.start, zeroToken, zero))
    oneToNineTokens foreach (t => {
        fsm = fsm.removeTransition(Transition(fsm.start, t, fsm.error))
        fsm = fsm.addTransition(Transition(fsm.start, t, number)) // '1' to '9'
    })
    aToZTokens foreach (t => {
        fsm = fsm.removeTransition(Transition(fsm.start, t, fsm.error))
        fsm = fsm.addTransition(Transition(fsm.start, t, variable))    // 'a' to 'z'
    })

    // From zero
    fsm = fsm.removeTransition(Transition(zero, capitalCaseXToken, fsm.error))
    fsm = fsm.addTransition(Transition(zero, capitalCaseXToken, hex))

    fsm = fsm.removeTransition(Transition(zero, lowerCaseXToken, fsm.error))
    fsm = fsm.addTransition(Transition(zero, lowerCaseXToken, hex))
    zeroToNineTokens foreach (t => {
        fsm = fsm.removeTransition(Transition(zero, t, fsm.error))
        fsm = fsm.addTransition(Transition(zero, t, number))             // '0' to '9'
    })

    // From hex
    zeroToNineTokens foreach (t => {
        fsm = fsm.removeTransition(Transition(hex, t, fsm.error))
        fsm = fsm.addTransition(Transition(hex, t, hex))                 // '0' to '9'
    })
    hexTokens foreach (t => {
        fsm = fsm.removeTransition(Transition(hex, t, fsm.error))
        fsm = fsm.addTransition(Transition(hex, t, hex))                 // 'A' to 'F'
    })

    // From number
    zeroToNineTokens foreach (t => {
        fsm = fsm.removeTransition(Transition(number, t, fsm.error))
        fsm = fsm.addTransition(Transition(number, t, number))           // '0' to '9'
    })

    // From variable
    zeroToNineTokens foreach (t => {
        fsm = fsm.removeTransition(Transition(variable, t, fsm.error))
        fsm = fsm.addTransition(Transition(variable, t, variable))       // '0' to '9'
    })
    aToZTokens foreach (t => {
        fsm = fsm.removeTransition(Transition(variable, t, fsm.error))
        fsm = fsm.addTransition((Transition(variable, t, variable)))     // 'a' to 'z'
    })

}
