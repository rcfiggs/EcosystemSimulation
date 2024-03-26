package ecoApp

case class Plant(birthday: Int) extends Organism {
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
      case event: EndDay =>
      this.energy += 1
      Seq(UpdateOrganismDisplay(this))
      case _ => Seq[Event]()
    }
}