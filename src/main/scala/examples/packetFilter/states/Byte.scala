package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._

case class Byte(val num: Int, val values: List[Int]) extends State {
    override def executeCode = {
        CodeManager.signal(this)
    }

    override def toString = {
        createString(1, values).dropRight(1)
    }

    private def createString(ct: Int, str: List[Int]): String = {
        str match {
            case Nil => ""
            case head :: next => 
                (if (head == -1) "*" else head.toHexString) + ":" + (if (ct % 6 == 0) "\n" else "") + createString(ct+1, next)
        }
    }

}