package model.entities
import ecoApp._
import view.UpdateEnviornmentDisplay
import scala.collection.mutable.Map


case class Rainfall(time: Long, amount: Int) extends Event {
  override val targetId = Entities.environment
}

case class Flood(time: Long, excessRainFall: Int) extends Event{
  override val targetId = Entities.entityManager
}

case class DeliverWater(override val targetId: Long, amount: Int) extends Event
case class DeliverNutrients(override val targetId: Long, amount: Int) extends Event

case object Environment extends Entity {
  val id: Long = Entities.environment
  val maxWaterInSoil: Int = 100
  val resources = Map[Resource, Int]()
  resources.addAll(Seq(
    (Resource.Water, 100),
    (Resource.Nutrient, 100)
  ))

  val rainfallEmitter = TimedEmitter(
    frequency = 30000, 
    eventGenerator = time => Rainfall(time, scala.util.Random.nextInt(45) + 15)
  )
  def eventEmitters: Seq[EventEmitter] = Seq(
    rainfallEmitter,
  )

  def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case Rainfall(time, amount) => {
      val excessRainfall = math.max(0, resources(Resource.Water) + amount - maxWaterInSoil)
      resources.update(Resource.Water, math.min(resources(Resource.Water) + amount, maxWaterInSoil))
      if (excessRainfall > 0) {
        Seq(Flood(time, excessRainfall), UpdateEnviornmentDisplay("Water", resources(Resource.Water).toString))
      } else {
        Seq(UpdateEnviornmentDisplay("Water", resources(Resource.Water).toString))
      }
    }
    case ExtractResource(_, resource, amount, sender) => {
      val deliverable = resources(resource) min amount
      resources.update(resource, resources(resource) - deliverable)
      Seq(resource match {
        case Resource.Water => UpdateEnviornmentDisplay("Water", resources(Resource.Water).toString)
        case Resource.Nutrient => UpdateEnviornmentDisplay("Nutrient", resources(Resource.Nutrient).toString)
        case _ => ???
      }) :+ ResourceGain(targetId = sender.id, resource = resource, amount = deliverable)
    }
  }
}
