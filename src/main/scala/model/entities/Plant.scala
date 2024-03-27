package ecoApp

case class ExtractWater(time: Long, amount: Int, senderId: Long) extends Event {
  override val targetId = Entities.environment
}

case class CheckWater[O <: Organism](organism: O) extends EventEmitter{
  override def emit(time: Long): Seq[Event] = {
    if (organism.hydration < 50) then Seq(ExtractWater(time, 100 - organism.hydration, organism.id))
    else Seq()
  }
}
case class Plant(birthday: Int) extends Organism {
  
  override val eventEmitters = super.eventEmitters :++ Seq(CheckWater(this))

  override def eventHandlers: PartialFunction[Event, Seq[Event]] = super.eventHandlers orElse {
      case event: EndDay =>
        this.energy += 1
        Seq(UpdateOrganismDisplay(this))
      case _ => Seq[Event]()
    }
}