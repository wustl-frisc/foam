
package foam
package aspects

object AroundState {
  def apply[A <: State](pointcut: Pointcut[A], base: NFA)(body: (StateJoinpoint[A], NFA) => (A, NFA)): NFA = {
    Advice[A, NFA](pointcut, base)((prevBase, point) => {

      val ins = Pointcutter.getIns(prevBase, point)
      val outs = Pointcutter.getOuts(prevBase, point)

      val joinPoints = if(ins.isEmpty && outs.isEmpty) {
        Set[StateJoinpoint[A]](StateJoinpoint[A](point, None, None))
      } else if (ins.nonEmpty && outs.isEmpty) {
        for(in <- ins) yield StateJoinpoint[A](point, Some(in), None)
      } else if (ins.isEmpty && outs.nonEmpty) {
        for(out <- outs) yield StateJoinpoint[A](point, None, Some(out))
      } else {
        for(in <- ins; out <- outs) yield StateJoinpoint[A](point, Some(in), Some(out))
      }

      val removeIns = ins.foldLeft(prevBase)((newBase, in) => newBase.removeTransition(in, point))
      val removeOuts = outs.foldLeft(removeIns)((newBase, out) => newBase.removeTransition((point, out._1), out._2))
      val removePoint = removeOuts.removeState(point)

      joinPoints.foldLeft(removePoint)((newBase, jp) => {
        val (advice, newNFA) = body(jp, newBase)

        (jp.in, jp.out) match {
          case (None, None) => newNFA
          case (Some(in), None) => newNFA.addTransition(in, advice)
          case (None, Some(out)) => newNFA.addTransition((advice, out._1), out._2)
          case (Some(in), Some(out)) => newNFA.addTransition(in, advice).addTransition((advice, out._1), out._2)
        }
      })
    })
  }
}

object AroundToken {
  def apply[A <: Token](pointcut: Pointcut[A], base: NFA)(body: (TokenJoinpoint[A], NFA) => (Option[Token], NFA)): NFA = {
    Advice[A, NFA](pointcut, base)((prevBase, point) => {
      val inOuts = Pointcutter.getInOuts(prevBase, point)
      val joinPoints = for(inOut <- inOuts) yield TokenJoinpoint[A](point, inOut._1, inOut._2)

      joinPoints.foldLeft(prevBase)((newBase, jp) => {
        val (advice, newNFA) = body(jp, newBase)

        advice match {
          case None => newNFA
          case Some(tokenAdvice) =>
            newNFA.removeTransition((jp.in, point), jp.out)
              .addTransition((jp.in, tokenAdvice), jp.out)
        }
      })
    })
  }
}
