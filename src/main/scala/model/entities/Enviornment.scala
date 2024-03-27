package model.entities
import ecoApp._

case class Rainfall(time: Long, amount: Int) extends Event {
  override val targetId = Entities.environment
}

case class Flood(time: Long, excessRainFall: Int) extends Event{
  override val targetId = Entities.entityManager
}

case object Environment extends Entity {
  val id: Long = Entities.newId
  var waterInSoil: Int = 0
  val maxWaterInSoil: Int = 100
  val eventEmitters: Seq[EventEmitter] = Seq(
    new RainfallEmitter,
    // Add more event emitters as needed
  )

  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
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

  def process(time: Long): Seq[Event] = {
    eventEmitters.flatMap(_.emit(time))
  }

  abstract class EventEmitter {
    def emit(time: Long): Seq[Event]
  }

  class RainfallEmitter extends EventEmitter {
    val frequency: Int = 30000 // frequency of rainfall events
    var lastEmittedTime: Long = 0

    override def emit(time: Long): Seq[Event] = {
      if (time >= lastEmittedTime + frequency) {
        lastEmittedTime = time
        val amount = scala.util.Random.nextInt(10) + 1 // random amount of rainfall
        Seq(Rainfall(time, amount))
      } else {
        Seq()
      }
    }
  }
}