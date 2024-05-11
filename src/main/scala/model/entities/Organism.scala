package model.entities

import model.events.{
  Event, ResourceLost, ExtractResource, ResourceGain, SpendResources, InsufficientResources,
  FindTarget, TargetFound, TargetNotFound,
  UpdateOrganismDisplay, Perished, IsPerished, Reproduce, CreateOrganism,
  EventEmitter, TimedEmitter, ConditionalEmitter,
  eventToSeq,
}
import model.resources.{
  Resource, Water,
  Conversion,
}
import model.dna.DNA
import organisms.{Plant, Fungi, Animal, PerishedOrganism}

import scala.collection.mutable

trait Organism extends Entity {
  val id: Long = Entities.newId
  val dna: DNA
  var target: Option[Long] = None
  
  val resources = mutable.Map[Resource, Int]()
  resources.addAll(dna.initialResources)
  val intakeRate: Map[Resource, Int] = dna.intakeRate
  val extractionRate: Map[Resource, Int] = dna.extractionRate
  val synthesisRate: Map[Conversion, Int] = dna.synthesisRate
  val resourceCapacities: Map[Resource, Int] = dna.resourceCapacities
  
  val waterLossEmitter = TimedEmitter(
  frequency = 5000,
  eventGenerator = (time) => ResourceLost(targetId = this.id, resource = Water, amount = 1)
  )
  
  val collect = TimedEmitter(
  frequency = 1000,
  eventGenerator = (time) => {
    val environmentalIntake = intakeRate.toSeq.flatMap { 
      case (resource, amount) => {
        val spaceAvailable = resourceCapacities.getOrElse(resource, Int.MaxValue) - resources.getOrElse(resource, 0)
        val amountToCollect = amount min spaceAvailable
        if (amountToCollect > 0) {
          Seq(ExtractResource(targetId = Entities.environment, resource = resource, amount = amountToCollect, sender = this))
        } else {
          Seq()
        }
      } 
    }
    val targetedExtraction = extractionRate.toSeq.flatMap { 
      case (resource, amount) => {
        target match {
          case Some(targetId) => {
            val spaceAvailable = resourceCapacities.getOrElse(resource, Int.MaxValue) - resources.getOrElse(resource, 0)
            val amountToCollect = amount min spaceAvailable
            if (amountToCollect > 0) {
              Seq(ExtractResource(targetId = targetId, resource = resource, amount = amountToCollect, sender = this))
            } else {
              Seq()
            }
          }
          case None => Seq()
        }
      } 
    }
    environmentalIntake ++ targetedExtraction
  }
  )
  
  val findTarget = TimedEmitter(
  frequency = 1000,
  eventGenerator = (time) => this match {
    case plant: Plant => FindTarget({case (_, o: Fungi) => o}, plant.id)
    case animal: Animal => FindTarget({case (_, o: Plant) => o}, animal.id)
    case fungi: Fungi => FindTarget({case (_, o: PerishedOrganism) => o}, fungi.id)
  }
  )
  
  val synthesize = TimedEmitter(
  frequency = 1000,
  eventGenerator = (time) => {
    synthesisRate.toSeq.flatMap { 
      case(conv: Conversion, maxAmount) => {
        val currentResourceAmounts = resources
        val gainedAmount = conv.inputs.map { case (resource, quantity) => (currentResourceAmounts.getOrElse(resource, 0) - 1) / quantity }.min
        val amount = gainedAmount min maxAmount
        if (amount > 0) {
          val spentResources = conv.inputs.map { case (resource, quantity) => resource -> quantity * amount }
          val gainedResources = conv.outputs.map { case (resource, quantity) => resource -> quantity * amount }
          
          // Check if the organism has enough resources to spend
          val canSpend = spentResources forall { case (resource, amount) => currentResourceAmounts.getOrElse(resource, 0) >= amount }
          
          // Check if the organism has enough capacity to hold the gained resources
          val canHold = gainedResources forall { case (resource, amount) => resourceCapacities.getOrElse(resource, Int.MaxValue) >= currentResourceAmounts.getOrElse(resource, 0) + amount }
          
          if (canSpend && canHold) {
            Seq(
            SpendResources(
            targetId = this.id, 
            resources = spentResources,
            resultingEvents = gainedResources.map { case (resource, amount) => ResourceGain(this.id, resource, amount) }.toSeq
            )
            )
          } else {
            Seq()
          }
        } else {
          Seq()
        }
      }
    }
  }
  )
  
  def eventEmitters: Seq[EventEmitter] = Seq(
  waterLossEmitter,
  synthesize,
  collect,
  findTarget,
  )
  
  def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case ResourceLost(_, resource: Resource, amount) => {
      val cur = resources.getOrElse(resource, 0)
      resources.update(resource, cur - (cur min amount))
      if(resources.getOrElse(resource, 0) <= 0) {
        println(s"$id ${this.getClass.getSimpleName} Died due to lack of ${resource.name}")
        Perished(this)
      } else{
        Seq()
      } :+ UpdateOrganismDisplay(this)
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
    case SpendResources(targetId, requiredResources: Map[Resource, Int], resultingEvents) => {
      val (sufficient, insufficient) = requiredResources.partition { case (resource, amount) => resources.getOrElse(resource, 0) >= amount }
      
      if (insufficient.isEmpty) {
        val resourcesLost = sufficient.map { case (resource, amount) => 
          val cur = resources.getOrElse(resource, 0)
          ResourceLost(id, resource, amount) 
        }
        (resultingEvents ++ resourcesLost) :+ UpdateOrganismDisplay(this)
      } else {
        InsufficientResources(targetId , insufficient)
      }
    }
    case InsufficientResources(targetId, resources) => {
      // println(s"Entity $targetId could not process resources $resources")
      Seq()
    }
    case TargetFound(_, foundId) => {
      target = Some(foundId)
      Seq()
    }
    case TargetNotFound(_) =>{
      target = None
      Seq()
    }
    case IsPerished(_, perishedOrganism) => target match {
      case Some(targetId) if targetId == perishedOrganism.id => {
        target = None
        Seq()
      }
      case _ => Seq()
    }
    case Reproduce(_, dnaMutation) => {
      val newResources = resources.map { case (resource, amount) => resource -> amount / 2 }.toMap
      val newDna = dna.withMutation(dnaMutation)
      val newOrganism = () => this match {
        case plant: Plant => Plant(newDna)
        case animal: Animal => Animal(newDna)
        case fungi: Fungi => Fungi(newDna)
      }
      newResources.foreach { case (resource, amount) => resources.update(resource, resources(resource) - amount) }
      Seq(
      UpdateOrganismDisplay(this),
      CreateOrganism(newOrganism),
      )
    }
  }
  
  
  def display: String = s"${this.getClass.getSimpleName}: ${resources.mkString(", ")}"
}