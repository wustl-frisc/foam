package edu.wustl.sbs
package fsm
package aspects

object BeforeState {
  def apply[A <: State](pointcut: Pointcut[A], base: NFA)(body: (StateJoinpoint[A], NFA) => (Option[(State, Token)], NFA)) = {
    Advice[A, NFA](pointcut, base)((prevBase, point) => {

      val ins = Pointcutter.getIns(prevBase, point)

      val joinPoints = for(in <- ins) yield (StateJoinpoint[A](point, Some(in), None))

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

object BeforeToken {
  def apply[A <: Token](pointcut: Pointcut[A], base: NFA)(body: (TokenJoinpoint[A], NFA) => (Option[(Token, State)], NFA)) = {
    Advice[A, NFA](pointcut, base)((prevBase, point) => {
      val inOuts = Pointcutter.getInOuts(prevBase, point)
      val joinPoints = for(inOut <- inOuts) yield (TokenJoinpoint[A](point, inOut._1, inOut._2))

      joinPoints.foldLeft(prevBase)((newBase, jp) => {
        val (advice, newNFA) = body(jp, newBase)

        advice match {
          case None => newNFA
          case Some(path) => {
            val (tokenAdvice, stateAdvice) = path
            newNFA.removeTransition((jp.in, point), jp.out)
            .addTransition((jp.in, tokenAdvice), stateAdvice)
            .addTransition((stateAdvice, point), jp.out)
          }
        }
      })
    })
  }
}
