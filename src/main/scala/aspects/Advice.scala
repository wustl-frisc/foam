package edu.wustl.sbs
package fsm
package featuredfsm
package aspects

trait Advice[A, B] {
  def apply(pointuct: A, base: B): B
}
