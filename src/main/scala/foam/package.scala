

package object foam {
  type TransitionKey = (State, Token)
  abstract class ChiselState extends State with HasCode
  abstract class ChiselToken extends Token with HasCond
}
