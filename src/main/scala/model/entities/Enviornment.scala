package model.entities
import ecoApp._
import view.UpdateEnviornmentDisplay


case class Rainfall(time: Long, amount: Int) extends Event {
  override val targetId = Entities.environment
}

case class Flood(time: Long, excessRainFall: Int) extends Event{
  override val targetId = Entities.entityManager
}

case class DeliverWater(override val targetId: Long, amount: Int) extends Event
case class ExtractNutrients(override val targetId: Long, amount: Int, senderId: Long) extends Event
case class DeliverNutrients(override val targetId: Long, amount: Int) extends Event

case object Environment extends Entity {
  val id: Long = Entities.environment
  var waterInSoil: Int = 50
  val maxWaterInSoil: Int = 100
  var nutrientsInSoil: Int = 1000

  val rainfallEmitter = TimedEmitter(
    frequency = 30000, 
    eventGenerator = time => Rainfall(time, scala.util.Random.nextInt(45) + 15)
  )
  def eventEmitters: Seq[EventEmitter] = Seq(
    rainfallEmitter,
    // Add more event emitters as needed
  )

  def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case Rainfall(time, amount) => {
      val excessRainfall = math.max(0, waterInSoil + amount - maxWaterInSoil)
      waterInSoil = math.min(waterInSoil + amount, maxWaterInSoil)
      if (excessRainfall > 0) {
        Seq(Flood(time, excessRainfall), UpdateEnviornmentDisplay("Water", waterInSoil.toString))
      } else {
        Seq(UpdateEnviornmentDisplay("Water", waterInSoil.toString))
      }
    }
    case ExtractWater(time, amount, senderId) => {
      val deliverable = Math.min(amount, waterInSoil)
      waterInSoil -= deliverable
      Seq(DeliverWater(targetId = senderId, amount = deliverable), UpdateEnviornmentDisplay("Water", waterInSoil.toString))
    }
    case ExtractNutrients(_, amount, senderId) => {
      val deliverable = Math.min(amount, nutrientsInSoil)
      nutrientsInSoil -= deliverable
      Seq(DeliverNutrients(targetId = senderId, amount = deliverable), UpdateEnviornmentDisplay("Water", waterInSoil.toString))
    }
    // Add more event handlers as needed
  }
}
