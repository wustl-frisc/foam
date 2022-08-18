package foam.examples

import foam.examples.{NamedState, NumericToken, TextToken}
import foam.product.CrossProduct
import foam.{Emitter, NFA, State}

object Cache {
  val sIdle = NamedState("sIdle", true)
  val sReadCache = NamedState("sReadCache", false)
  val sWriteCache = NamedState("sWriteCache", false)
  val sWriteBack = NamedState("sWriteBack", false)
  val sWriteAck = NamedState("sWriteAck", false)
  val sRefillReady = NamedState("sRefillReady", false)
  val sRefill = NamedState("sRefill", false)
  val sBusRd = NamedState("sBusRd", false)
  val sBusRdX = NamedState("sBusRdX", false)

  val readReq = TextToken("readReq")
  val writeReq = TextToken("writeReq")
  val readHit = TextToken("readHit")
  val writeHit = TextToken("writeHit")
  val readFinish = TextToken("readFinish")
  val dirtyMiss = TextToken("dirtyMiss")
  val cleanMiss = TextToken("cleanMiss")
  val writeFinish = TextToken("writeFinish")
  val dirtyMissAdv = TextToken("dirtyMiss")
  val cleanMissAdv = TextToken("cleanMiss")
  val ack = TextToken("ack")
  val refillReady = TextToken("refillReady")
  val doRefill = TextToken("doRefill")
  val refillFinish = TextToken("refillFinish")
  val doWrite = TextToken("doWrite")
  val busRdToken = TextToken("busRd")
  val busRdXToken = TextToken("busRdX")

  def apply() = {
    val cacheNFA = (new NFA(sIdle))
    .addTransition((sIdle, readReq), sReadCache)
    .addTransition((sIdle, writeReq), sWriteCache)
    .addTransition((sReadCache, readHit), sReadCache)
    .addTransition((sReadCache, readFinish), sIdle)
    .addTransition((sReadCache, dirtyMiss), sBusRd)
    .addTransition((sBusRd, dirtyMiss), sWriteBack)
    .addTransition((sReadCache, cleanMiss), sBusRd)
    .addTransition((sBusRd, cleanMiss), sRefill)
    .addTransition((sReadCache, writeHit), sWriteCache)
    .addTransition((sWriteCache, writeFinish), sIdle)
    .addTransition((sWriteCache, dirtyMissAdv), sBusRdX)
    .addTransition((sBusRdX, dirtyMissAdv), sWriteBack)
    .addTransition((sWriteCache, cleanMissAdv), sBusRdX)
    .addTransition((sBusRdX, cleanMissAdv), sRefill)
    .addTransition((sWriteBack, ack), sWriteAck)
    .addTransition((sWriteAck, refillReady), sRefillReady)
    .addTransition((sRefillReady, doRefill), sRefill)
    .addTransition((sRefill, refillFinish), sIdle)
    .addTransition((sRefill, doWrite), sWriteCache)
    .addTransition((sIdle, busRdToken), sWriteBack)
    .addTransition((sIdle, busRdXToken), sWriteBack)

    var result = cacheNFA
    for(i <- 0 to 1) {
      result = CrossProduct(result, cacheNFA)
    }
    println(s"${result.states.size},${result.alphabet.size},${result.transitions.size}")
  }
}