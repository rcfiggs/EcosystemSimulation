package ecoApp

import scala.collection.mutable.Map

enum Resource {
  case Water, Energy, Nutrient
}

trait Organism extends Entity {
  import Resource._
  val id: Long = Entities.newId
  val birthday: Int
  val resources = Map[Resource, Int]()
  resources.addAll(Seq(
    (Water, 100),
    (Energy, 100),
    (Nutrient, 100)
  ))

  val waterLossEmitter = TimedEmitter[ResourceLost] (
    frequency = 1000,
    eventGenerator = (time) => ResourceLost(targetId = this.id, resource = Water, amount = 1)
  )

  def eventEmitters: Seq[EventEmitter] = Seq(
    waterLossEmitter,
  )
  
  def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case ResourceLost(_, resource, amount) => {
      val cur = resources(resource)
      resources.update(resource, cur - (cur min amount))
      (resource match {
        case Water if (resources(Water) <= 0) => Seq(Perished(this))
        case _ => Seq()
      }) :+ UpdateOrganismDisplay(this)
    }
    case ResourceGain(_, resource, amount) => {
      val cur = resources(resource)
      resources.update(resource, cur + amount)
      Seq(UpdateOrganismDisplay(this))
    }
    case ExtractResource(_, resource, amount, sender) => {
      val deliverable = resources(resource) min amount
      Seq(
        ResourceLost(targetId = this.id, resource = resource, amount = deliverable),
        ResourceGain(targetId = sender.id, resource = resource, amount = deliverable),
      )
    }
  }
  
  def display: String = s"${this.getClass.getSimpleName}: Energy: ${resources(Energy)}, Hydration: ${resources(Water)}, Nutrients: ${resources(Nutrient)}"
}

case class ResourceLost(targetId: Long, resource: Resource, amount: Int) extends Event

case class ResourceGain(targetId: Long, resource: Resource, amount: Int) extends Event

case class ExtractResource(targetId: Long, resource: Resource, amount: Int, sender: Organism) extends Event

case class Perished(organism: Organism) extends Event{
  override val targetId = Entities.entityManager
}

case class IsPerished(targetId: Long, organism: PerishedOrganism) extends Event

case class PerishedOrganism(override val id: Long, birthday: Int) extends Organism{

  override val eventEmitters: Seq[EventEmitter] = Seq()

  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case ExtractResource(_, resource, amount, sender) =>
      sender match {
        case animal: Animal => Seq(IsPerished(sender.id, this))
        case fungi: Fungi => Seq() // TODO add fungi extaction logic
        case _ => Seq() 
      }
    case event: Event => super.eventHandlers(event) // Other events are handled by the Organism trait
  }
}