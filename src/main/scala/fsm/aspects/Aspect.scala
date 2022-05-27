package edu.wustl.sbs
package fsm
package aspects

trait Aspect[A] {
  def apply(base: A):A
}
