package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._
import aspects._

object PacketFilter {

    val start = SimpleStateFactory()
    private val accept = SimpleStateFactory()
    private val error = SimpleStateFactory()
    val protocol = SimpleStateFactory()
    val srcPort = SimpleStateFactory()
    val destPort = SimpleStateFactory()
    val srcIp1 = SimpleStateFactory()
    val srcIp2 = SimpleStateFactory()
    val srcIp3 = SimpleStateFactory()
    val srcIp4 = SimpleStateFactory()
    val destIp1 = SimpleStateFactory()
    val destIp2 = SimpleStateFactory()
    val destIp3 = SimpleStateFactory()
    val allow = SimpleStateFactory()
    val deny = SimpleStateFactory()

    val nameMap = Map[State, String](
        start -> "Start", 
        accept -> "Accept", 
        error -> "Error",
        protocol -> "Protocol",
        srcPort -> "Source Port",
        destPort -> "Dest Port",
        srcIp1 -> "Source *.?.?.?",
        srcIp2 -> "Source *.*.?.?",
        srcIp3 -> "Source *.*.*.?",
        srcIp4 -> "Source *.*.*.*",
        destIp1 -> "Dest *.?.?.?",
        destIp2 -> "Dest *.*.?.?",
        destIp3 -> "Dest *.*.*.?",
        allow -> "Allow",
        deny -> "Deny",
    )
    val stateMap = nameMap.map(_.swap)

    def apply() = {

        val fsm = new NFA(start, accept, error)
            .addTransition((start, Protocol("TCP")), protocol)
            .addTransition((start, Protocol("UDP")), protocol)
            .addTransition((protocol, Asterisk), srcPort)
            .addTransition((srcPort, Asterisk), destPort)
            .addTransition((destPort, Asterisk), srcIp1)
            .addTransition((srcIp1, Asterisk), srcIp2)
            .addTransition((srcIp2, Asterisk), srcIp3)
            .addTransition((srcIp3, Asterisk), srcIp4)
            .addTransition((srcIp4, Asterisk), destIp1)
            .addTransition((destIp1, Asterisk), destIp2)
            .addTransition((destIp2, Asterisk), destIp3)
            .addTransition((destIp3, Asterisk), deny)
            .addTransition((deny, Lambda), accept)
            .addTransition((allow, Lambda), accept)

        val features = Set(Example)
        val finalFSM = Weaver[NFA](features, fsm, (before: NFA, after: NFA) => before.isEqual(after))

        finalFSM
    }
}