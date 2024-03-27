package ecoApp

import model.entities.DeliverWater

case class FoundPlant(targetId: Long, plantId: Long) extends Event
case class NoPlantFound(targetId: Long) extends Event

case class RequestWater(senderId:Long, targetId:Long) extends Event

case class SearchForWater(time: Long, senderId: Long) extends Event {
  override val targetId = Entities.entityManager
}
case class Animal(birthday: Int) extends Organism {

  val checkWater = ConditionalEmitter[SearchForWater](
    condition = () => hydration < 20, // Threshold for thirst
    eventGenerator = (time) => SearchForWater(time, this.id)
  )

  override def eventEmitters = super.eventEmitters :++ Seq(checkWater)

  override def eventHandlers: PartialFunction[Event, Seq[Event]] = super.eventHandlers orElse {
    case event: FoundPlant =>
    // logic to store the found plant
    Seq(ExtractWater(senderId = id, targetId = event.plantId, amount = 100 - hydration))
  case event: DeliverWater =>
    hydration = hydration + event.amount
    Seq(UpdateOrganismDisplay(this))
  }
}