package model.entities.organisms

import model.entities.{Organism}
import model.entities.organisms.{Animal, Fungi}
import model.dna.{DNA}
import model.events.{
  Event, IsPerished, ExtractResource, DecomposeResource,
  EventEmitter, eventToSeq,
}
import model.resources.{Resource, Conversion}
import model.resources.ResourceContainer
import model.events.UpdateOrganismDisplay

case class PerishedOrganism(
override val id: Long,
override val resources: scala.collection.mutable.Map[Resource,Int],
override val dna: DNA = DNA(properties = Map()),
) extends Organism with ResourceContainer {
  override val targetable = (o: Organism) => false
  override val eventEmitters: Seq[EventEmitter] = Seq()

  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case ExtractResource(_, resource, amount, sender) =>
    sender match {
      case animal: Animal => Seq(IsPerished(sender.id, this))
      case fungi: Fungi => Seq()
      case _ => Seq() 
    }
    case DecomposeResource(_, conversion, amount) => {
      // Calculate the number of conversions that can happen without exceeding any of the necessary ingredients
      val maxConversions = conversion.inputs.map { case (resource, quantity) => resources(resource) / quantity }.min
      
      // Cap the amount at the maximum number of conversions
      val actualAmount = math.min(amount, maxConversions)
      
      // Remove the input resources
      val inputs = conversion.inputs.map { case (resource, quantity) => 
        resource -> quantity * actualAmount
      }
      inputs.flatMap { case (resource, amount) => updateResources(resource, -amount) }
      
      // Add the output resources
      val outputs = conversion.outputs.map { case (resource, quantity) => 
        resource -> quantity * actualAmount
      }
      outputs.flatMap { case (resource, amount) => updateResources(resource, amount) }
      displayEventFactory()
    }
    case event: Event => super.eventHandlers(event)
  }
}