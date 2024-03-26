package ecoApp

trait Organism extends Entity {
  val id: Long = Entities.newId
  val birthday: Int
  var energy: Int = 100
  var hydration: Int = 100

  val upkeep = List[UpkeepEventer](WaterLossEventer(this.id))

  override def update(): Seq[Event] = {
    val organismHandlers: PartialFunction[Event, Seq[Event]] = {
      case WaterLost(_, amount, time) =>  {
        hydration -= amount
        if (hydration <= 0) {
          Seq(Perished(this, time))
        } else Seq() 
      }
      case Perished(_, time) => Seq()
  } 
    events.dequeueAll(_ => true).flatMap(organismHandlers orElse eventHandlers)
  }
  def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case WaterLost(_, amount, time) =>  {
      hydration -= amount
      if (hydration <= 0) {
        Seq(Perished(this, time))
      } else Seq() 
    }
    case Perished(_, time) => Seq()
  }
  
  def process(time: Long): Seq[Event] = {
    val upkeepEvents = upkeep.flatMap {
      case eventer: UpkeepEventer => {
        if (time >= eventer.lastEmittedTime + eventer.frequency) {
          eventer.lastEmittedTime = time
          Seq(eventer.event(time))
        } else Seq()
          
      } 
      case _ => Seq()
    }
    if (upkeepEvents.nonEmpty) upkeepEvents :+ UpdateOrganismDisplay(this)
    else upkeepEvents
  }
  
  def display: String = s"${this.getClass.getSimpleName}: Energy: $energy, Hydration: $hydration"
}

trait UpkeepEventer {
  val event: (time: Long) => Event
  val frequency: Long
  var lastEmittedTime: Long
}

case class WaterLost(targetId:Long, amount: Int, time: Long) extends Event
case class WaterLossEventer(id: Long) extends UpkeepEventer {
  val event = (time: Long) => WaterLost(id, 1, time)
  val frequency: Long = 5000
  var lastEmittedTime: Long = 0
}

case class Perished(organism: Organism, time: Long) extends Event{
  override val targetId = Entities.entityManager
}

case class PerishedOrganism(targetId: Long, birthday: Int) extends Organism{
  override def update() = Seq()
  override def process(time: Long): Seq[Event] = Seq()
}