package edu.wustl.sbs
package aspects

object Weaver {
  def apply[A](aspectSet: Set[_ <: Aspect[A]], base: A, isEqual: (A, A) => Boolean): A = {
    val finalBase = aspectSet.foldLeft(base)((newBase, aspect) => aspect(newBase))
    if (!isEqual(base, finalBase)) apply(aspectSet, finalBase, isEqual)
    else finalBase
  }
}
