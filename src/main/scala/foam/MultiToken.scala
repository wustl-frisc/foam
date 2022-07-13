package foam

case class MultiToken(t: Seq[Token], override val isLamda: Boolean) extends Token {
  override def toString: String = t map(_.toString) reduceLeft(_ + " and " + _)
}

object MultiTokenFactory {

  def apply(t: Seq[Token]): Token = {

    if (t.size == 1) {
      t.toList.head
    } else if (t.size > 1) {
      var cumulativeSeq = Seq[Token]()
      for (token: Token <- t) {
        token match {
          case MultiToken(set, _) => cumulativeSeq = cumulativeSeq ++ set
          case anyOther => cumulativeSeq = cumulativeSeq :+ anyOther
        }
      }

      // The cumulative token is a lambda token iff every token passed is marked as Lambda.
      val isLambda = cumulativeSeq.forall(_.isLamda)

      MultiToken(cumulativeSeq, isLambda)
    } else {
      throw new IllegalArgumentException("Can't create a MultiToken with an empty set")
    }
  }
}