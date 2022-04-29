package edu.wustl.sbs
package examples

import fsm._
import aspects._

class IPV4Rule(srcIp: String = "*.*.*.*", destIp: String = "*.*.*.*", protocol: String = "*", srcPort: String = "*", destPort: String = "*") extends Aspect[NFA] {

    def apply(nfa: NFA) = {

        val destinationPort = if (destPort == "*") List(-1, -1) else List(destPort.toInt>>>8.toByte, destPort.toInt.toByte).map((i) => if (i < 0) i+256 else i)
        val sourcePort = if (srcPort == "*") List(-1, -1) else List(srcPort.toInt>>>8.toByte, srcPort.toInt.toByte)
        val prot = if (protocol == "TCP") 6 else if (protocol == "UDP") 17 else if (protocol == "*") -1 else protocol.toInt
        val sourceIp = srcIp.split(raw"\.").map((c) => if (c == "*") "-1" else c).map(_.toInt).toList
        val destinationIp = destIp.split(raw"\.").map((c) => if (c == "*") "-1" else c).map(_.toInt).toList

        var bytes = List(4,-1,-1,-1, -1,-1,-1,-1, -1,prot,-1,-1) ++ sourceIp ++ destinationIp ++ sourcePort ++ destinationPort
        assert(bytes.size == 24)

        // Select all the states on our path
        val statePointCut: Pointcut[Byte] = Pointcutter[State, Byte](nfa.states, state => state match {
            case s: Byte =>
                s.num < 24 && s.values == bytes.take(s.num)
            case _ => false
        })
        
        val nfa2 = Following[Byte](statePointCut, nfa)(
            (thisJoinPoint: Byte) => (if (bytes(thisJoinPoint.num) == -1) Lambda else Bits(bytes(thisJoinPoint.num)), Byte(thisJoinPoint.num+1, bytes.take(thisJoinPoint.num+1)))
        )

        Following[Byte](Set(Byte(24, bytes)), nfa2)((joinPoint: Byte) => (Lambda, PacketFilter.deny))
    }
}