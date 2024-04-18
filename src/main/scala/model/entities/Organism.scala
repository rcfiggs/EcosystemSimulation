package ecoApp

import scala.collection.mutable.Map
import scala.collection.mutable.Set
import model.Resources._
import scala.collection.immutable

trait OrganismComponent

trait Organism extends Entity {
  
  val id: Long = Entities.newId
  val birthday: Int
  val acquire: PartialFunction[Resource, Seq[Event]]
  val resources = Map[Resource, Int]()
  resources.addAll(Seq(
  (Water, 100),
  (Energy, 25),
  (Nutrient, 25)
  ))
  
  val waterLossEmitter = TimedEmitter[ResourceLost] (
  frequency = 5000,
  eventGenerator = (time) => ResourceLost(targetId = this.id, resource = Water, amount = 1)
  )
  
  def eventEmitters: Seq[EventEmitter] = Seq(
  waterLossEmitter,
  )
  
  def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case ResourceLost(_, resource: Resource, amount) => {
      val cur = resources.getOrElse(resource, 0)
      resources.update(resource, cur - (cur min amount))
      (resource match {
        case Water if (resources.getOrElse(Water, 0) <= 0) => Seq(Perished(this))
        case _ => Seq()
      }) :+ UpdateOrganismDisplay(this)
    }
    case ResourceGain(_, resource: Resource, amount) => {
      val cur = resources.getOrElse(resource, 0)
      resources.update(resource, cur + amount)
      Seq(UpdateOrganismDisplay(this))
    }
    case ExtractResource(_, resource: Resource, amount, sender) => {
      val deliverable = resources.getOrElse(resource,0) min amount
      Seq(
      ResourceLost(targetId = this.id, resource = resource, amount = deliverable),
      ResourceGain(targetId = sender.id, resource = resource, amount = deliverable),
      )
    }
    case SpendResources(targetId, requiredResources: immutable.Map[Resource, Int], resultingEvents) => {
      val (sufficient, insufficient) = requiredResources.partition { case (resource, amount) => resources.getOrElse(resource, 0) >= amount }
      
      if (insufficient.isEmpty) {
        sufficient.foreach { case (resource, amount) => 
          val cur = resources.getOrElse(resource, 0)
          resources.update(resource, cur - amount) 
        }
        resultingEvents :+ UpdateOrganismDisplay(this)
      } else {
        val out = insufficient.toSeq.flatMap {
          (resource, amount) => {
            Seq.fill(amount)(acquire(resource)).flatMap(a => a)
          }
        } :+ AwaitingResources(
          targetId = targetId,
          requiredResources = requiredResources,
          pendingEvent = SpendResources(targetId, requiredResources, resultingEvents)
        )

        out

      }
    }
    case AwaitingResources(targetId, requiredResources, pendingEvent) => {
      if (requiredResources.forall { case (resource, amount) => resources.getOrElse(resource, 0) >= amount }) {
        // If all resources are available, fire off the pending events
        Seq(pendingEvent)
      } else {
        // If not all resources are available, return this event again
        Seq(AwaitingResources(targetId, requiredResources, pendingEvent))
      }
    }
  }
  
  
  def display: String = s"${this.getClass.getSimpleName}: ${resources.toString}"
}

case class AwaitingResources(targetId: Long, requiredResources: immutable.Map[Resource, Int], pendingEvent: SpendResources) extends Event
case class ResourceLost(targetId: Long, resource: Resource, amount: Int) extends Event

case class ResourceGain(targetId: Long, resource: Resource, amount: Int) extends Event

case class ExtractResource(targetId: Long, resource: Resource, amount: Int, sender: Organism) extends Event

case class SpendResources(targetId: Long, resources: immutable.Map[Resource, Int], resultingEvents: Seq[Event] = Seq.empty) extends Event

case class Perished(organism: Organism) extends Event{
  override val targetId = Entities.entityManager
}

case class IsPerished(targetId: Long, organism: PerishedOrganism) extends Event

case class PerishedOrganism(override val id: Long, birthday: Int) extends Organism {
  
  override val acquire = {
    case _ => Seq()
  }

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