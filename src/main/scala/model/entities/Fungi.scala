package ecoApp

case class Fungi(birthday: Int) extends Organism {
  
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: Rain =>
    this.hydration += 10
    Seq(UpdateOrganismDisplay(this))
    case _ => Seq[Event]()
  }
  
  override def hydrationLossRate(time: Long): Int = 1
}
