package model.resources

import model.events.{Event, ExtractResource, ResourceGain, ResourceLost, ResourceDrained}
import model.entities.Entity

trait ResourceContainer {
  self: Entity =>

  val displayEventFactory: () => Event

  val resources: scala.collection.mutable.Map[Resource, Int]
  def updateResources(resource: Resource, amount: Int): Seq[Event] = {
    val newAmount = resources.getOrElse(resource, 0) + amount
    resources.update(resource, newAmount)
    if (newAmount <= 0) {
      Seq(ResourceDrained(self.id, resource))
    } else {
      Seq()
    }
  }

  def resourceContainerEventHandlers: PartialFunction[Event, Seq[Event]] = {
    case ExtractResource(_, resource: Resource, amount, sender) => {
      val deliverable = resources.getOrElse(resource, 0) min amount
      updateResources(resource, -deliverable) ++ Seq(
        ResourceGain(targetId = sender.id, resource = resource, amount = deliverable),
        displayEventFactory(),
      )
    }
    case ResourceGain(_, resource: Resource, amount) => {
      updateResources(resource, amount)
    }
    case ResourceLost(_, resource: Resource, amount) => {
      updateResources(resource, -amount)
    }
  }
}