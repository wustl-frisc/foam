package edu.wustl.sbs
package examples

import fsm._
import aspects._

class Base(threshold: Int) extends Aspect[NFA] {
    def apply(nfa: NFA) = {

        // Select all the states with only asterisks
        val statePointCut: Pointcut[Byte] = Pointcutter[State, Byte](nfa.states, state => state match {
            case s: Byte if (s.num < threshold) =>
                val set = s.values.toSet
                (set.size == 1 && set.contains(-1)) || s.num == 0
            case _ => false
        })
        
        val nfa2 = Following[Byte](statePointCut, nfa)((thisJoinPoint: Byte) => (Asterisk, Byte(thisJoinPoint.num+1, -1::thisJoinPoint.values)))

        val nfa3 = Following[Byte](Set(Byte(threshold, List.fill(threshold)(-1))), nfa2)((thisJoinPoint: Byte) => (Lambda, PacketFilter.allow))

        Following[State](Set(PacketFilter.deny, PacketFilter.allow), nfa3)((joinPoint: State) => (Lambda, nfa3.acceptState))
    }
}