package ecoApp

case class ExtractWater(time: Long, amount: Int, senderId: Long) extends Event {
  override val targetId = Entities.environment
}

case class Plant(birthday: Int) extends Organism {

  val checkWater = ConditionEmitter[ExtractWater](
    condition = hydration < 50,
    eventGenerator = (time) => ExtractWater(time, 100 - hydration, this.id)
  )
  
  override def eventEmitters = super.eventEmitters :++ Seq(checkWater)

  override def eventHandlers: PartialFunction[Event, Seq[Event]] = super.eventHandlers orElse {
      case event: EndDay =>
        this.energy += 1
        Seq(UpdateOrganismDisplay(this))
    }
}