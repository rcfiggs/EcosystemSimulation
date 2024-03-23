package ecoApp

case class Plant(birthday: Int) extends Organism {
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: Sunshine =>
    this.energy += 10
    Seq(UpdateOrganismDisplay(this))
    case _ => Seq[Event]()
  }
  
  override def hydrationLossRate(timeDifference: Long): Int = {
    // lose 1 water per second
    (timeDifference / 1000).toInt
  }
}