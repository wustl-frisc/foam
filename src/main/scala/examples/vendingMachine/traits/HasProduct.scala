package edu.wustl.sbs
package examples

import fsm._

trait HasProduct {
  this: Component =>
  val product: Product
  override def toString = super.toString + " " + product.toString
}
