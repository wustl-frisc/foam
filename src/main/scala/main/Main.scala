package main
import fsm._
import featuredfsm._
import scalaclient._

object Main extends App {

    val tokenizer = Tokenizer.fsm

    println("\n\n")
    tokenizer.accept(List[Token](Character('0'), Character('x'), Character('1'), Character('2'), Character(' ')))
    println("\n")
    tokenizer.accept(List[Token](Character('0'), Character('1'), Character('2'), Character(' ')))
    println("\n")
    tokenizer.accept(List[Token](Character('x'), Character('0'), Character('1'), Character('2'), Character(' ')))
    println("\n\n")

}
