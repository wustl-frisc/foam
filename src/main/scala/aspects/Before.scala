package edu.wustl.sbs
package aspects

import fsm._

object Before {
  def apply[A <: State](pointcut: Pointcut[A], base: NFA)(body: (Joinpoint[A], NFA) => (Option[(State, Token)], NFA)) = {
    Advice[A, NFA](pointcut, base)((prevBase, point) => {

      val ins = prevBase.getIns(point)

      val joinPoints = for(in <- ins) yield (Joinpoint[A](point, Some(in), None))

      joinPoints.foldLeft(prevBase)((newBase, jp) => {
        val (advice, newNFA) = body(jp, newBase)

        advice match {
          case None => newNFA
          case Some(path) => {
            var (stateAdvice, tokenAdvice) = path

            newNFA.removeTransition(jp.in.get, point)
            .addTransition(jp.in.get, stateAdvice)
            .addTransition((stateAdvice, tokenAdvice), point)
          }
        }
      })
    })
  }
}
