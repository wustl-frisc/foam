package edu.wustl.sbs
package fsm
package featuredfsm
package aspects

object Weaver {
  def apply[A <: FSM](aspectSet: Set[Aspect[A]], fsm: A) = {
    applyHelper[A](aspectSet, fsm)
  }

  private def applyHelper[A <: FSM](aspectSet: Set[Aspect[A]], fsm: A): A = {
    val finalFSM = aspectSet.foldLeft(fsm)((newFSM, a) => a(newFSM))
    if(!finalFSM.isEqual(fsm)) applyHelper(aspectSet, finalFSM)
    else finalFSM
  }
}
