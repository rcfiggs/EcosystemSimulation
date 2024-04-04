package ecoApp

import model.entities.DeliverWater
import Resource._

case class FoundPlant(targetId: Long, plantId: Long) extends Event
case class NoPlantFound(targetId: Long) extends Event

case class SearchForWater(senderId: Long) extends Event {
  override val targetId = Entities.entityManager
  println("Searching for water")
}
case class Animal(birthday: Int) extends Organism {

  var needsWater: Boolean = false

  val checkWater = ConditionalEmitter[SearchForWater](
    condition = () => { 
      if(!needsWater && resources(Water) < 80){
        needsWater = true
        true
      } else {
        false
      }
    },
    eventGenerator = (_) => SearchForWater(this.id)
  )

  override def eventEmitters = super.eventEmitters :++ Seq(checkWater)

  override def eventHandlers: PartialFunction[Event, Seq[Event]] = super.eventHandlers orElse {
    case event: FoundPlant =>
    // logic to store the found plant
    Seq(ExtractWater(senderId = this.id, amount = 100 - resources(Water), targetId = event.plantId))
  case event: DeliverWater =>
    needsWater = false
    Seq(ResourceGain(this.id, event.amount, Water), UpdateOrganismDisplay(this))
  }
}