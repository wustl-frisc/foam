package edu.wustl.sbs
package aspects

object Pointcutter {
  def apply[A](base: Iterable[A], pred: A => Boolean): Pointcut[A] = {
    base.foldLeft(Pointcut[A]())((pointcut, joinpoint) => if (pred(joinpoint)) pointcut + joinpoint else pointcut)
  }
}
