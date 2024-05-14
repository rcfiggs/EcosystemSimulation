package model.entities.organisms

import model.entities.{Organism}
import model.entities.organisms.{Animal, Fungi}
import model.dna.{DNA}
import model.events.{
  Event, IsPerished, ExtractResource, DecomposeResource,
  EventEmitter,
}
import model.resources.{Resource, Conversion}

case class PerishedOrganism(
override val id: Long,
override val resources: scala.collection.mutable.Map[Resource,Int],
override val dna: DNA = DNA(properties = Map()),
) extends Organism {
  override val targetable = (o: Organism) => false
  override val eventEmitters: Seq[EventEmitter] = Seq()
  
  def getDecompositionConversion(resource: Resource): Conversion = {
    fungiDNA.properties.collectFirst { case (Decomposition(conversion), _) if conversion.inputs.contains(resource) => conversion }.get
  }
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case ExtractResource(_, resource, amount, sender) =>
    sender match {
      case animal: Animal => Seq(IsPerished(sender.id, this))
      case fungi: Fungi => Seq()
      case _ => Seq() 
    }
    case DecomposeResource(resource, amount) => {
      val conversion = getDecompositionConversion(resource)
      val inputs = conversion.inputs.map { case (resource, quantity) => resource -> quantity * amount }
      inputs.flatMap { case (resource, amount) => updateResources(resource, -amount) }.toSeq
    }   
    case event: Event => super.eventHandlers(event)
  }
}