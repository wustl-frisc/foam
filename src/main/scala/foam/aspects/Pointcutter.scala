
package foam
package aspects

object Pointcutter {
  def apply[A, B <: A](base: Iterable[A], pred: A => Boolean): Pointcut[B] = {
    base.foldLeft(Pointcut[A]())((pointcut, joinpoint) => if (pred(joinpoint)) pointcut + joinpoint else pointcut).asInstanceOf[Pointcut[B]]
  }


  def getIns(fsm: FSM, state: State): Iterable[(State, Token)] = {
    fsm.transitions.keys.filter(key => key match {
      case _: TransitionKey if fsm.transitions(key) contains state  => true
      case _ => false
    })
  }

  def getOuts(fsm: FSM, state: State): Iterable[(Token, State)] = {
    val keys = fsm.transitions.keys.filter(key => key match {
      case _: TransitionKey if key._1 == state => true
      case _ => false
    })

    for(k <- keys; d <- fsm.transitions(k)) yield (k._2, d)
  }

  def getInOuts(fsm: FSM, token: Token): Set[(State, State)] = {
    for(in <- fsm.states; out <- fsm.transitions((in, token))) yield (in, out)
  }
}
