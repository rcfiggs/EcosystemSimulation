package ecoApp

trait Organism extends Entity {
  val id: Long = Entities.newId
  val birthday: Int
  var energy: Int = 100
  var hydration: Int = 100
  var nutrients: Int = 100

  val waterLossEmitter = TimedEmitter[WaterLost] (
    frequency = 1000,
    eventGenerator = (time) => WaterLost(this.id, 1)
  )

  def eventEmitters: Seq[EventEmitter] = Seq(
    waterLossEmitter,
  )
  
  def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case WaterLost(_, amount) =>  {
      hydration -= amount
      if (hydration <= 0) {
        Seq(Perished(this), UpdateOrganismDisplay(this))
      } else Seq(UpdateOrganismDisplay(this)) 
    }
  }
  
  def display: String = s"${this.getClass.getSimpleName}: Energy: $energy, Hydration: $hydration"
}

case class WaterLost(targetId:Long, amount: Int) extends Event

case class Perished(organism: Organism) extends Event{
  override val targetId = Entities.entityManager
}

case class PerishedOrganism(override val id: Long, birthday: Int) extends Organism{

  override val eventEmitters: Seq[EventEmitter] = Seq()

  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case _ => Seq()
  }
}