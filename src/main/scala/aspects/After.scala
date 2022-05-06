package edu.wustl.sbs
package aspects

import fsm._

object AfterState {
  def apply[A <: State](pointcut: Pointcut[A], base: NFA)(body: (StateJoinpoint[A], NFA) => (Option[(Token, State)], NFA)) = {
    Advice[A, NFA](pointcut, base)((prevBase, point) => {

      val outs = Pointcutter.getOuts(prevBase, point)

      if(outs.isEmpty) {
        val (advice, newNFA) = body(StateJoinpoint[A](point, None, None), prevBase)
        advice match {
          case None => newNFA
          case Some(path) => {
            var (tokenAdvice, stateAdvice) = path
            newNFA.addTransition((point, tokenAdvice), stateAdvice)
          }
        }
      } else {
        val joinPoints = for(out <- outs) yield (StateJoinpoint[A](point, None, Some(out)))

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

object AfterToken {
  def apply[A <: Token](pointcut: Pointcut[A], base: NFA)(body: (TokenJoinpoint[A], NFA) => (Option[(State, Token)], NFA)) = {
    Advice[A, NFA](pointcut, base)((prevBase, point) => {
      val inOuts = Pointcutter.getInOuts(prevBase, point)
      val joinPoints = for(inOut <- inOuts) yield (TokenJoinpoint[A](point, inOut._1, inOut._2))

      joinPoints.foldLeft(prevBase)((newBase, jp) => {
        val (advice, newNFA) = body(jp, newBase)

        advice match {
          case None => newNFA
          case Some(path) => {
            val (stateAdvice, tokenAdvice) = path
            newNFA.removeTransition((jp.in, point), jp.out)
            .addTransition((jp.in, point), stateAdvice)
            .addTransition((stateAdvice, tokenAdvice), jp.out)
          }
        }
      })
    })
  }
}
