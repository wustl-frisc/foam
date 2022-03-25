package edu.wustl.sbs
package fsm
package featuredfsm
package aspects

import fsm.featuredfsm._

trait Aspect[T] {
  def apply(base: T): T
}
