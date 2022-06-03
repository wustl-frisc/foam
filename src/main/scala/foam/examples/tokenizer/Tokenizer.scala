
package foam
package examples

// Currently accpts lists of digits, lowercase letters
object Tokenizer {

  val start = SimpleStateFactory(false)
  implicit var fsm = new NFA(start)

  val zero = SimpleStateFactory(false)
  val hex = SimpleStateFactory(false)
  val number = SimpleStateFactory(false)
  val variable = SimpleStateFactory(false)
  val acceptNumber = SimpleStateFactory(true)
  val acceptHex = SimpleStateFactory(true)
  val acceptVariable = SimpleStateFactory(true)
  val spaceToken = Character(' ')
  val preAcceptStates = Set[State](acceptNumber, acceptHex, acceptVariable)


  fsm = fsm.addTransition((zero, spaceToken), acceptNumber)

  fsm = fsm.addTransition((number, spaceToken), acceptNumber)

  fsm = fsm.addTransition((hex, spaceToken), acceptHex)

  fsm = fsm.addTransition((variable, spaceToken), acceptVariable)

  val zeroToken = Character('0')
  val capitalCaseXToken = Character('X')
  val lowerCaseXToken = Character('x')
  val oneToNineTokens = for (i <- 0 to 8) yield (Character((49 + i).toChar))
  val zeroToNineTokens = Set(zeroToken) ++ oneToNineTokens
  val aToZTokens = for (i <- 0 to 25) yield (Character((97 + i).toChar))
  val hexTokens = for (i <- 0 to 5) yield (Character((65 + i).toChar))
  val allTokens = Set(zeroToken) ++ Set(capitalCaseXToken) ++ Set(lowerCaseXToken) ++ oneToNineTokens ++ aToZTokens ++ hexTokens

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
