package ecoApp

import Resource._

case class Plant(birthday: Int) extends Organism {

  val checkWater = ConditionalEmitter[ExtractResource](
    condition = () => (resources(Water) < 95),
    eventGenerator = (_) => Some(ExtractResource(targetId = Entities.environment, amount = 100 - resources(Water), sender = this, resource = Resource.Water))
  )
  
  override def eventEmitters = super.eventEmitters :++ Seq(checkWater)

}