package ecoApp

import model.entities.DeliverWater
import model.entities.ExtractNutrients
import Resource._

case class ExtractWater(targetId: Long, amount: Int, senderId: Long) extends Event 

case class Plant(birthday: Int) extends Organism {

  private var waterRequested: Boolean = false

  val checkWater = ConditionalEmitter[ExtractWater](
    condition = () => {
      if(!waterRequested && resources(Water) < 95){
        waterRequested = true;
        true
      } else false
    },
    eventGenerator = (_) => ExtractWater(targetId = Entities.environment, amount = 100 - resources(Water), senderId = this.id)
  )

  val gatherNutrients = ConditionalEmitter[ExtractNutrients](
    condition = () => {
      if(!waterRequested && resources(Water) < 95){
        waterRequested = true;
        true
      } else false
    },
    eventGenerator = (_) => ExtractNutrients(targetId = Entities.environment, amount = 100 - resources(Nutrient), senderId = this.id)
  )
  
  override def eventEmitters = super.eventEmitters :++ Seq(checkWater)

}