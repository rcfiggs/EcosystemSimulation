package model.entities

import model.resources.{Resource, NaturalResource, Water, Nutrient, Sunlight, ResourceContainer}
import model.events.{
  Event, Rainfall, UpdateEnviornmentDisplay, Flood, ExtractResource, ResourceGain,
  EventEmitter, TimedEmitter,
  eventToSeq,
}

import scala.collection.mutable.Map
import model.resources.ResourceContainer

case class Environment() extends Entity with ResourceContainer {
  val id: Long = Entities.environment
  val maxWaterInSoil: Int = 10000
  val displayEventFactory: () => Event = () => UpdateEnviornmentDisplay(this)
  val resources = Map[Resource, Int]()
  resources.addAll(Seq(
    (Water, 10000),
    (Nutrient, 100),
    (Sunlight, Int.MaxValue)
  ))

  // val rainfallEmitter = TimedEmitter(
  //   frequency = 30000, 
  //   eventGenerator = time => Rainfall(time, scala.util.Random.nextInt(4500) + 1500)
  // )
  def eventEmitters: Seq[EventEmitter] = Seq(
    // rainfallEmitter,
  )

  def eventHandlers: PartialFunction[Event, Seq[Event]] = resourceContainerEventHandlers orElse {
    case Rainfall(time, amount) => {
      val excessRainfall = math.max(0, resources(Water) + amount - maxWaterInSoil)
      resources.update(Water, math.min(resources(Water) + amount, maxWaterInSoil))
      if (excessRainfall > 0) {
        Seq(Flood(time, excessRainfall), UpdateEnviornmentDisplay(this))
      } else {
        Seq(UpdateEnviornmentDisplay(this))
      }
    }
    // case ExtractResource(_, resource: NaturalResource, amount, sender) => {
    //   val deliverable = resources.getOrElse(resource, 0) min amount
    //   resources.update(resource, resources.getOrElse(resource, 0) - deliverable)
    //   Seq(
    //     UpdateEnviornmentDisplay(this),
    //     ResourceGain(targetId = sender.id, resource = resource, amount = deliverable),
    //   )
      
    // }
  }
}
