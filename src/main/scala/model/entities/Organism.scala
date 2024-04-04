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
    eventGenerator = (time) => ResourceLost(this.id, 1, Water)
  )

  def eventEmitters: Seq[EventEmitter] = Seq(
    waterLossEmitter,
  )
  
  def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case ResourceLost(_, amount, resource) => {
      val cur = resources(resource)
      resources.update(resource, cur - (cur min amount))
      Seq(UpdateOrganismDisplay(this))
    }
    case ResourceGain(_, amount, resource) => {
      val cur =  resources(resource)
      resources.update(resource, cur + amount)
      Seq(UpdateOrganismDisplay(this))
    }
  }
  
  def display: String = s"${this.getClass.getSimpleName}: Energy: ${resources(Energy)}, Hydration: ${resources(Water)}, Nutrients: ${resources(Nutrient)}"
}

case class ResourceLost(targetId: Long, amount: Int, resource: Resource) extends Event

case class ResourceGain(targetId: Long, amount: Int, resource: Resource) extends Event

case class WaterLost(targetId:Long, amount: Int) extends Event

case class Perished(organism: Organism) extends Event{
  override val targetId = Entities.entityManager
}

case class PerishedOrganism(override val id: Long, birthday: Int) extends Organism{

  override val eventEmitters: Seq[EventEmitter] = Seq()

  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case _ => Seq()
  }
}