package edu.wustl.sbs
package examples

import fsm._
import aspects._

object Example extends Aspect[NFA] {

    def apply(nfa: NFA) = {

        val PF = PacketFilter

        val modified = nfa
            .addTransition((PF.start, Protocol("TCP")), ProtocolState("TCP"))
            .addTransition((ProtocolState("TCP"), PortNumber(25)), SrcPort(25))
            .addTransition((SrcPort(25), PortNumber(8080)), DestPort(8080))
            .addTransition((DestPort(8080), IPBits(167)), SrcIP1(167))
            .addTransition((SrcIP1(167), IPBits(205)), SrcIP2(205))
            .addTransition((SrcIP2(205), IPBits(3)), SrcIP3(3))
            .addTransition((SrcIP3(3), IPBits(11)), SrcIP4(11))
            .addTransition((SrcIP4(11), IPBits(167)), DestIP1(167))
            .addTransition((DestIP1(167), IPBits(205)), DestIP2(205))
            .addTransition((DestIP2(205), IPBits(65)), DestIP3(65))
            .addTransition((DestIP3(65), IPBits(32)), PF.allow)

            .addTransition((ProtocolState("TCP"), Asterisk), PF.srcPort)
            .addTransition((SrcPort(25), Asterisk), PF.destPort)
            .addTransition((DestPort(8080), Asterisk), PF.srcIp1)
            .addTransition((SrcIP1(167), Asterisk), PF.srcIp2)
            .addTransition((SrcIP2(205), Asterisk), PF.srcIp3)
            .addTransition((SrcIP3(3), Asterisk), PF.srcIp4)
            .addTransition((SrcIP4(11), Asterisk), PF.destIp1)
            .addTransition((DestIP1(167), Asterisk), PF.destIp2)
            .addTransition((DestIP2(205), Asterisk), PF.destIp3)
            .addTransition((DestIP3(65), Asterisk), PF.deny)

        modified
    }

}