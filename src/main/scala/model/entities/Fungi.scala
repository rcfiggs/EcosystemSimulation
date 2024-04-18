package ecoApp

case class Fungi(birthday: Int) extends Organism {
  override val acquire = {
    case _ => Seq()
  }
}
