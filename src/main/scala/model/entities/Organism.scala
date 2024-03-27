package ecoApp

trait Organism extends Entity {
  val id: Long = Entities.newId
  val birthday: Int
  var energy: Int = 100
  var hydration: Int = 100

  def eventEmitters: Seq[EventEmitter] = Seq(
    WaterLossEmitter(this.id),
  )
  
  def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case WaterLost(_, amount, time) =>  {
      hydration -= amount
      if (hydration <= 0) {
        Seq(Perished(this, time), UpdateOrganismDisplay(this))
      } else Seq(UpdateOrganismDisplay(this)) 
    }
  }
  
  def display: String = s"${this.getClass.getSimpleName}: Energy: $energy, Hydration: $hydration"
}

case class WaterLost(targetId:Long, amount: Int, time: Long) extends Event
case class WaterLossEmitter(id: Long) extends TimedEmitter[WaterLost] (
  frequency = 5000,
  eventGenerator = (time) => WaterLost(id, 1, time),
)

case class Perished(organism: Organism, time: Long) extends Event{
  override val targetId = Entities.entityManager
}

case class PerishedOrganism(targetId: Long, birthday: Int) extends Organism{

  override val eventEmitters: Seq[EventEmitter] = Seq()

  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case _ => Seq()
  }
}