package main
import fsm._
import featuredfsm._
import scalaclient._

object Main extends App {

    val numFinder = Tokenizer.fsm

    println(numFinder.accept(List[Token]()))

}
