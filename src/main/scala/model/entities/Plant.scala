package ecoApp

import model.entities.DeliverWater
import model.entities.ExtractNutrients
import Resource._

case class ExtractWater(targetId: Long, amount: Int, sender: Organism) extends Event 

case class Plant(birthday: Int) extends Organism {

  val checkWater = ConditionalEmitter[ExtractWater](
    condition = () => (resources(Water) < 95),
    eventGenerator = (_) => Some(ExtractWater(targetId = Entities.environment, amount = 100 - resources(Water), sender = this))
  )
  
  override def eventEmitters = super.eventEmitters :++ Seq(checkWater)

}