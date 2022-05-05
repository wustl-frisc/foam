package edu.wustl.sbs
package aspects

import fsm._

case class Joinpoint[A](val point: A, val in: Option[TransitionKey], val out: Option[(Token, State)])
