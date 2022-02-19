package fsm

final case class Lambda() extends Token {
    override def isLamda: Boolean = true
}
