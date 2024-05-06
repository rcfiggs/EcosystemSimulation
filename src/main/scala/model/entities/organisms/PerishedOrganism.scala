package model.entities.organisms

import model.entities.{Organism}
import model.entities.organisms.{Animal, Fungi}
import model.dna.{DNA}
import model.events.{
  Event, IsPerished, ExtractResource,
  EventEmitter,
}
import model.resources.Resource

case class PerishedOrganism(override val id: Long, override val dna: DNA, override val initialResources: Map[Resource, Int] = Map()) extends Organism {
  override val eventEmitters: Seq[EventEmitter] = Seq()
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case ExtractResource(_, resource, amount, sender) =>
    sender match {
      case animal: Animal => Seq(IsPerished(sender.id, this))
      case fungi: Fungi => Seq()
      case _ => Seq() 
    }
    case event: Event => super.eventHandlers(event)
  }
}