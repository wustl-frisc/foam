package edu.wustl.sbs
package aspects

import fsm._

object After {
  def apply[A <: State](pointcut: Pointcut[A], base: NFA)(body: (Joinpoint[A], NFA) => (Option[(Token, State)], NFA)) = {
    Advice[A, NFA](pointcut, base)((prevBase, point) => {

      val outs = prevBase.getOuts(point)

      if(outs.isEmpty) {
        val (advice, newNFA) = body(Joinpoint[A](point, None, None), prevBase)
        advice match {
          case None => newNFA
          case Some(path) => {
            var (tokenAdvice, stateAdvice) = path
            newNFA.addTransition((point, tokenAdvice), stateAdvice)
          }
        }
      } else {
        val joinPoints = for(out <- outs) yield (Joinpoint[A](point, None, Some(out)))

        joinPoints.foldLeft(prevBase)((newBase, jp) => {
          val (advice, newNFA) = body(jp, newBase)

          advice match {
            case None => newNFA
            case Some(path) => {
              val (tokenAdvice, stateAdvice) = path
              val out = jp.out.get

              newNFA.removeTransition((point, out._1), out._2)
              .addTransition((point, tokenAdvice), stateAdvice)
              .addTransition((stateAdvice, out._1), out._2)
            }
          }
        })
      }
    })
  }
}
