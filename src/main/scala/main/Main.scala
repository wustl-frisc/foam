package main
import fsm._
import featuredfsm._
import scalaclient._

object Main extends App {

    val tokenizer = Tokenizer.fsm
    tokenizer.accept(List[Token](Character('0'), Character('X'), Character('1'), Character(' ')))

}
