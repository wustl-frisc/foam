package edu.wustl.sbs
package examples

import fsm._
import fsm.featuredfsm._
import aspects._

object PacketFilter {

    val start = Byte(0, Nil)
    private val accept = SimpleStateFactory()
    private val error = SimpleStateFactory()
    val allow = SimpleStateFactory()
    val deny = SimpleStateFactory()

    val nameMap = Map[State, String](
        start -> "Start", 
        accept -> "Accept", 
        error -> "Error",
        allow -> "Allow",
        deny -> "Deny",
    )
    val stateMap = nameMap.map(_.swap)

    def apply() = {

        val fsm = new NFA(start, accept, error)

        val features = Set(new Base(24), new IPV4Rule("167.205.3.11", "167.205.65.32", "TCP", "25", "8080"))
        val finalFSM = Weaver[NFA](features, fsm, (before: NFA, after: NFA) => before.isEqual(after))

        finalFSM
    }
}