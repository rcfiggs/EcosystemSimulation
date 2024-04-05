package ecoApp

import model.entities.DeliverWater
import Resource._

case class FoundPlant(targetId: Long, plantId: Long) extends Event
case class NoPlantFound(targetId: Long) extends Event

case class SearchForPlant(senderId: Long) extends Event {
  override val targetId = Entities.entityManager
  println("Searching for plant")
}
case class Animal(birthday: Int) extends Organism {
  
  var targetPlant: Option[Long] = None
  
  val checkPlant = ConditionalEmitter[SearchForPlant](
  condition = () => !targetPlant.isDefined,
  eventGenerator = (_) => Some(SearchForPlant(this.id))
  )
  
  val checkWater = ConditionalEmitter[ExtractResource](
  condition = () => targetPlant.isDefined && resources(Water) < 80,
  eventGenerator = (_) => targetPlant match {
    case Some(plantId) => Some(ExtractResource(resource = Water, targetId = plantId, amount = 100 - resources(Water), sender = this))
    case None => None
  }
  )
  
  override def eventEmitters = super.eventEmitters :++ Seq(checkPlant, checkWater)
  
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = super.eventHandlers orElse {
    case FoundPlant(_, plantId) =>
      targetPlant = Some(plantId)
      Seq()
    case NoPlantFound(_) => 
      targetPlant = None
      Seq() 
    case IsPerished(_, perishedOrganism) => targetPlant match {
      case Some(plantId) if plantId == perishedOrganism.id => {
        targetPlant = None
        Seq()
      }
      case _ => Seq()
    }
  }
}