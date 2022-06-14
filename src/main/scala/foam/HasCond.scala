package foam

import chisel3._

trait HasCond extends Component {
  val cond: Bool
}