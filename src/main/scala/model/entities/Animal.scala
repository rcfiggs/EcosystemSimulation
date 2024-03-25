package ecoApp

case class Animal(birthday: Int) extends Organism {
  
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: Sunshine =>
    this.energy -= 10
    Seq(UpdateOrganismDisplay(this))
    case _ => Seq[Event]()
  }
  
}