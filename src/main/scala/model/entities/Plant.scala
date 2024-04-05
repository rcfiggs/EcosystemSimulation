package ecoApp

import Resource._

case class Plant(birthday: Int) extends Organism {
  
  val checkWater = ConditionalEmitter[ExtractResource](
    condition = () => (resources(Water) < 95),
    eventGenerator = (_) => Some(ExtractResource(targetId = Entities.environment, amount = 100 - resources(Water), sender = this, resource = Resource.Water))
  )
  
  val extractNutrients = ConditionalEmitter[SpendResource](
    condition = () => resources(Nutrient) < 100 && resources(Energy) > 5,
    eventGenerator = (_) => Some(SpendResource(
      targetId = this.id, 
      resource = Energy, 
      amount = 5, 
      resultingEvent = ExtractResource(
        targetId = Entities.environment, 
        resource = Nutrient,
        amount = 10, // Or whatever amount you need
        sender = this
      ) 
    ))
  )
  
  override def eventEmitters = super.eventEmitters :++ Seq(checkWater, extractNutrients)
  
}