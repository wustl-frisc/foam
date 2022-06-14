package foam

trait HasCode extends Component {
  val code: () => Unit
}