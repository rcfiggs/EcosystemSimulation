package model.events

import model.entities.{Entity, Entities, Organism, Environment}
import model.entities.organisms.PerishedOrganism
import model.dna.{DNA, DNAEntry, DNAMutation}
import model.resources.{Resource, NaturalResource}
import scala.language.implicitConversions
import model.entities.Environment

sealed trait Event {
  def targetId: Long
  def name: String = this.getClass.getSimpleName
}

implicit def eventToSeq(event: Event): Seq[Event] = Seq(event)

// Game State Manager Events
sealed trait GameStateManagerEvent extends Event {
  override val targetId = Entities.gameStateManager
}
case class CreateOrganism(newOrganism: () => Organism) extends GameStateManagerEvent
case object Play extends GameStateManagerEvent 
case object Pause extends GameStateManagerEvent 
case class Forward(events: Seq[Event]) extends GameStateManagerEvent 

// Environment Events
case class Rainfall(time: Long, amount: Int) extends Event {
  override val targetId = Entities.environment
}
case class Flood(time: Long, excessRainFall: Int) extends Event{
  override val targetId = Entities.gameStateManager
}
case class ReturnResourcesToEnvironment(resources: Map[Resource, Int]) extends Event {
  override val targetId = Entities.environment
}

// Resource Events
case class ResourceLost(targetId: Long, resource: Resource, amount: Int) extends Event
case class ResourceGain(targetId: Long, resource: Resource, amount: Int) extends Event
case class ExtractResource(targetId: Long, resource: Resource, amount: Int, sender: Organism) extends Event
case class ResourceDrained(targetId: Long, resource: Resource) extends Event

// Organism Events
case class Reproduce(targetId: Long, dnaMutation: DNAMutation) extends Event

case class FindTarget(predicate: Organism => Boolean, senderId: Long) extends Event {
  override val targetId = Entities.gameStateManager
}
case class TargetFound(targetId: Long, foundId: Long) extends Event
case class TargetNotFound(targetId: Long) extends Event

case class InsufficientResources(targetId: Long, resources: Map[Resource, Int]) extends Event
case class SpendResources(targetId: Long, resources: Map[Resource, Int], resultingEvents: Seq[Event] = Seq.empty) extends Event
case class Perished(organism: Organism) extends Event {
  override val targetId = Entities.gameStateManager
}
case class IsPerished(targetId: Long, organism: PerishedOrganism) extends Event

// UI
case class ButtonPressed(targetId: Long) extends Event

// Environment Display
case class UpdateEnviornmentDisplay(environment: Environment) extends Event {
  override val targetId = Entities.environmentDisplay
}

// Organism Display
case class AddOrganismToDisplay(organism: Organism) extends Event {
  override val targetId = Entities.organismDisplay
}
case class RemoveOrganismFromDisplay(id: Long) extends Event {
  override val targetId = Entities.organismDisplay
}
case class UpdateOrganismDisplay(organism: Organism) extends Event {
  override val targetId = Entities.organismDisplay
}
case class OrganismSelected(targetId: Long, organism: Organism) extends Event
case class RemoveFromDisplay(id: Long) extends Event {
  override val targetId = Entities.organismDisplay
}

// DNA Display
case class UpdateDNADisplay(dna: DNA) extends Event {
  override val targetId = Entities.dnaDisplay
}

case class DNAEntrySelected(targetId: Long, dnaEntry: DNAEntry) extends Event

// Testing
case class TestEvent(override val targetId: Long = 0) extends Event