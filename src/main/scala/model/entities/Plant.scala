package ecoApp

import model.entities.DeliverWater

case class ExtractWater(targetId: Long,amount: Int, senderId: Long) extends Event

case class Plant(birthday: Int) extends Organism {

  var waterRequested: Boolean = false

  val checkWater = ConditionalEmitter[ExtractWater](
    condition = () => {
      if(!waterRequested && hydration < 95){
        waterRequested = true;
        true
      } else false
    },
    eventGenerator = (_) => ExtractWater(
      targetId = Entities.environment, 
      amount = 100 - hydration, 
      senderId = this.id
    )
  )
  
  override def eventEmitters = super.eventEmitters :++ Seq(checkWater)

  override def eventHandlers: PartialFunction[Event, Seq[Event]] = super.eventHandlers orElse {
      case event: EndDay =>
        this.energy += 1
        Seq(UpdateOrganismDisplay(this))
      case event: DeliverWater =>
        hydration = hydration + event.amount
        waterRequested = false
        Seq(UpdateOrganismDisplay(this))
    }
}