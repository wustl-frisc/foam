package edu.wustl.sbs
package aspects

class Aspect[A](transform: A => A) {
  def apply(base: A) = transform(base)
}
