package edu.wustl.sbs
package aspects

import fsm._

object Around {
  def apply[A <: State](pointcut: Pointcut[A], base: NFA)(body: (StateJoinpoint[A], NFA) => (A, NFA)) = {
    Advice[A, NFA](pointcut, base)((prevBase, point) => {

      val ins = Pointcutter.getIns(prevBase, point)
      val outs = Pointcutter.getOuts(prevBase, point)

      val joinPoints = if(ins.isEmpty) {
        Set[StateJoinpoint[A]](StateJoinpoint[A](point, None, None))
      } else if (outs.isEmpty) {
        for(in <- ins) yield (StateJoinpoint[A](point, Some(in), None))
      } else  {
        for(in <- ins; out <- outs) yield (StateJoinpoint[A](point, Some(in), Some(out)))
      }

      val removeIns = ins.foldLeft(prevBase)((newBase, in) => newBase.removeTransition(in, point))
      val removeOuts = outs.foldLeft(removeIns)((newBase, out) => newBase.removeTransition((point, out._1), out._2))

      joinPoints.foldLeft(removeOuts)((newBase, jp) => {
        val (advice, newNFA) = body(jp, newBase)

        jp.in match {
          case None => newNFA
          case Some(in) => {
            val step1 = newNFA.addTransition(in, advice)
            jp.out match {
              case None => step1
              case Some(out) => {
                step1.addTransition((advice, out._1), out._2)
              }
            }
          }
        }
      })
    })
  }
}
