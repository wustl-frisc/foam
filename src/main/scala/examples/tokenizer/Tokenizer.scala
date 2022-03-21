package examples
import fsm._
import featuredfsm._
import aspects._

// Currently accpts lists of digits, lowercase letters
object Tokenizer {

  var token: String = ""

  val start = SimpleStateFactory()
  val acceptState = SimpleStateFactory()
  val error = SimpleStateFactory()
  implicit var fsm = FeatureOrientedFSM(start, acceptState, error)

  val zero = SimpleStateFactory()
  fsm = fsm.addState(zero, "zero")

  val hex = SimpleStateFactory()
  fsm = fsm.addState(hex, "hex")

  val number = SimpleStateFactory()
  fsm = fsm.addState(number, "number")

  val variable = SimpleStateFactory()
  fsm = fsm.addState(variable, "variable")

  val normalStates = Set[State](zero, hex, number, variable)
  for (state <- normalStates) {
    CodeManager.addCode(state, (x) => {
      token += (
        if (x == Lambda) "" else x)
    })
  }
  CodeManager.addCode(start, (x) => { // Adding code to start state
    token = ""
  })

  val acceptNumber = SimpleStateFactory()
  fsm = fsm.addState(acceptNumber, "acceptNumber")

  val acceptHex = SimpleStateFactory()
  fsm = fsm.addState(acceptHex, "acceptHex")

  val acceptVariable = SimpleStateFactory()
  fsm = fsm.addState(acceptVariable, "acceptVariable")

  val spaceToken = Character(' ')
  fsm = fsm.addToken(spaceToken)
  val preAcceptStates = Set[State](acceptNumber, acceptHex, acceptVariable)
  fsm = fsm.addTransition((zero, spaceToken), acceptNumber)

  fsm = fsm.addTransition((number, spaceToken), acceptNumber)

  fsm = fsm.addTransition((hex, spaceToken), acceptHex)

  fsm = fsm.addTransition((variable, spaceToken), acceptVariable)

  for (state <- preAcceptStates) {
    fsm = fsm.addTransition((state, Lambda), fsm.acceptState)
  }
  CodeManager.addCode(acceptNumber, (x) => { println("Found a number value: " + token.toInt) })
  CodeManager.addCode(acceptHex, (x) => { println("Found a hexadecimal value: " + token) })
  CodeManager.addCode(acceptVariable, (x) => { println("Found a variable: " + token) })

  val zeroToken = Character('0')
  val capitalCaseXToken = Character('X')
  val lowerCaseXToken = Character('x')
  val oneToNineTokens = for (i <- 0 to 8) yield (Character((49 + i).toChar))
  val zeroToNineTokens = Set(zeroToken) ++ oneToNineTokens
  val aToZTokens = for (i <- 0 to 25) yield (Character((97 + i).toChar))
  val hexTokens = for (i <- 0 to 5) yield (Character((65 + i).toChar))
  val allTokens = Set(zeroToken) ++ Set(capitalCaseXToken) ++ Set(lowerCaseXToken) ++ oneToNineTokens ++ aToZTokens ++ hexTokens
  allTokens foreach (t => {
    fsm = fsm.addToken(t)
  })

  // From start
  fsm = fsm.addTransition((fsm.start, zeroToken), zero)
  oneToNineTokens foreach (t => {
    fsm = fsm.addTransition((fsm.start, t), number) // '1' to '9'
  })
  aToZTokens foreach (t => {
    fsm = fsm.addTransition((fsm.start, t), variable) // 'a' to 'z'
  })

  // From zero
  fsm = fsm.addTransition((zero, capitalCaseXToken), hex)

  fsm = fsm.addTransition((zero, lowerCaseXToken), hex)
  zeroToNineTokens foreach (t => {
    fsm = fsm.addTransition((zero, t), number) // '0' to '9'
  })

  // From hex
  zeroToNineTokens foreach (t => {
    fsm = fsm.addTransition((hex, t), hex) // '0' to '9'
  })
  hexTokens foreach (t => {
    fsm = fsm.addTransition((hex, t), hex) // 'A' to 'F'
  })

  // From number
  zeroToNineTokens foreach (t => {
    fsm = fsm.addTransition((number, t), number) // '0' to '9'
  })

  // From variable
  zeroToNineTokens foreach (t => {
    fsm = fsm.addTransition((variable, t), variable) // '0' to '9'
  })
  aToZTokens foreach (t => {
    fsm = fsm.addTransition((variable, t), variable) // 'a' to 'z'
  })
}
