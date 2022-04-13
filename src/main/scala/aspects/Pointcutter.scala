package edu.wustl.sbs
package aspects

object Pointcutter {
  def apply[A, B <: A](base: Iterable[A], pred: A => Boolean): Pointcut[B] = {
    base.foldLeft(Pointcut[A]())((pointcut, joinpoint) => if (pred(joinpoint)) pointcut + joinpoint else pointcut).asInstanceOf[Pointcut[B]]
  }
}
