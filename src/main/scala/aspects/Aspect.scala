package edu.wustl.sbs
package fsm
package featuredfsm
package aspects

trait Aspect[T] {
  def apply(base: T): T
}
