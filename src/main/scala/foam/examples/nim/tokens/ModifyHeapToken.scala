package foam.examples.nim.tokens

import foam.Token
import foam.examples.nim.aspects.HeapModification

case class ModifyHeapToken(modifications: Seq[HeapModification]) extends Token {

  override def toString: String = {

//    if (modifications.length == 1) {
//      val modification = modifications.head
//      modification.action + " " + Math.abs(modification.delta)
//    } else {
      modifications.mkString(",")
//    }
  }

}
