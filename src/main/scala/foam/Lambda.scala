
package foam

object Lambda extends Token {
    override def isLamda: Boolean = true

    override def toString(): String = "λ"
}
