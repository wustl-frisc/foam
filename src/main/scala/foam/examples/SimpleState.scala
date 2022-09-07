
package foam
package examples

private final case class SimpleState(override val id: String, val isAccept: Boolean) extends State {

    override def toString(): String = {
        "SimpleState" + id
    }

}

object SimpleStateFactory {

    private var stateCount = 0;

    def apply(isAccept: Boolean): State = {
        stateCount += 1
        SimpleState(stateCount.toString, isAccept)
    }
}
