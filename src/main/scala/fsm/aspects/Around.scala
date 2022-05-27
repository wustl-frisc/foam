package edu.wustl.sbs
package fsm
package aspects

object AroundState {
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
      val removePoint = removeOuts.removeState(point)

      joinPoints.foldLeft(removePoint)((newBase, jp) => {
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

object AroundToken {
  def apply[A <: Token](pointcut: Pointcut[A], base: NFA)(body: (TokenJoinpoint[A], NFA) => (Option[Token], NFA)) = {
    Advice[A, NFA](pointcut, base)((prevBase, point) => {
      val inOuts = Pointcutter.getInOuts(prevBase, point)
      val joinPoints = for(inOut <- inOuts) yield (TokenJoinpoint[A](point, inOut._1, inOut._2))

      joinPoints.foldLeft(prevBase)((newBase, jp) => {
        val (advice, newNFA) = body(jp, newBase)

        advice match {
          case None => newNFA
          case Some(path) => {
            val tokenAdvice = path
            newNFA.removeTransition((jp.in, point), jp.out)
            .addTransition((jp.in, tokenAdvice), jp.out)
          }
        }
      })
    })
  }
}
