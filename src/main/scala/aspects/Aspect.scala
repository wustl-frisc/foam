package edu.wustl.sbs
package aspects

trait Aspect[A] {
  def apply(base: A):A
}
