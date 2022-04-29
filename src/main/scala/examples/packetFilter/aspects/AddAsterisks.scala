package edu.wustl.sbs
package examples

import fsm._
import aspects._
import os.stat

class AddAsterisks() extends Aspect[NFA] {

    def apply(nfa: NFA) = {

        // Select states in the path with no Asterisk or Lambda transition
        val statePointCut: Pointcut[Byte] = Pointcutter[State, Byte](nfa.states, state => state match {
            case s: Byte => 
                nfa.transitions((s, Asterisk)).contains(nfa.error) && nfa.transitions((s, Lambda)).size == 0
            case _ => false
        })

        Following[Byte](statePointCut, nfa)(
            (joinPoint: Byte) => (Asterisk, Byte(joinPoint.num+1, List.fill(joinPoint.num+1)(-1)))
        )
    }
}