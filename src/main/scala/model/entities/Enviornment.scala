package model.entities
import ecoApp._

case class Rainfall(time: Long, amount: Int) extends Event {
  override val targetId = Entities.environment
}

case object RainfallEmitter extends TimedEmitter[Rainfall] (
  frequency = 30000, 
  eventGenerator = time => Rainfall(time, scala.util.Random.nextInt(10) + 1)
)

case class Flood(time: Long, excessRainFall: Int) extends Event{
  override val targetId = Entities.entityManager
}

case object Environment extends Entity {
  val id: Long = Entities.newId
  var waterInSoil: Int = 0
  val maxWaterInSoil: Int = 100
  def eventEmitters: Seq[EventEmitter] = Seq(
    RainfallEmitter,
    // Add more event emitters as needed
  )

  def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case Rainfall(time, amount) => {
      val excessRainfall = math.max(0, waterInSoil + amount - maxWaterInSoil)
      waterInSoil = math.min(waterInSoil + amount, maxWaterInSoil)
      if (excessRainfall > 0) {
        Seq(Flood(time, excessRainfall))
      } else {
        Seq()
      }
    }
    // Add more event handlers as needed
  }
}
