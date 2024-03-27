package ecoApp

import model.entities.DeliverWater

case class FoundPlant(targetId: Long, plantId: Long) extends Event
case class NoPlantFound(targetId: Long) extends Event

case class SearchForWater(senderId: Long) extends Event {
  override val targetId = Entities.entityManager
}
case class Animal(birthday: Int) extends Organism {

  val checkWater = ConditionalEmitter[SearchForWater](
    condition = () => hydration < 80, // Threshold for thirst
    eventGenerator = (_) => SearchForWater(this.id)
  )

  override def eventEmitters = super.eventEmitters :++ Seq(checkWater)

  override def eventHandlers: PartialFunction[Event, Seq[Event]] = super.eventHandlers orElse {
    case event: FoundPlant =>
    // logic to store the found plant
    Seq(ExtractWater(senderId = this.id, amount = 100 - hydration, targetId = event.plantId))
  case event: DeliverWater =>
    hydration = hydration + event.amount
    Seq(UpdateOrganismDisplay(this))
  }
}