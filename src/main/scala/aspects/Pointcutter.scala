package edu.wustl.sbs
package aspects

import fsm._

object Pointcutter {
  def apply[A, B <: A](base: Iterable[A], pred: A => Boolean): Pointcut[B] = {
    base.foldLeft(Pointcut[A]())((pointcut, joinpoint) => if (pred(joinpoint)) pointcut + joinpoint else pointcut).asInstanceOf[Pointcut[B]]
  }

  def getIns(fsm: FSM, state: State) = {
    fsm.transitions.keys.filter(key => key match {
      case k: TransitionKey if(fsm.transitions(key) contains state)  => true
      case _ => false
    })
  }

  def getOuts(fsm: FSM, state: State) = {
    val keys = fsm.transitions.keys.filter(key => key match {
      case k: TransitionKey if(key._1 == state) => true
      case _ => false
    })

    for(k <- keys; d <- fsm.transitions(k)) yield (k._2, d)
  }
}
