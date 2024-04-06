package ecoApp

import model.Resources._

case class Plant(birthday: Int, var roots: Int = 1, var leaves: Int = 2, var stems: Int = 1) extends Organism {

  def aboveGroundWeight: Int = stems * 2 + leaves
  def rootStrength: Int = roots * 2
  def leafSupport: Int = stems * 2
  
  val checkWater = ConditionalEmitter[ExtractResource[EnvironmentalResource]](
    condition = () => (resources(Water) < 95),
    eventGenerator = (_) => Some(ExtractResource(targetId = Entities.environment, amount = 100 - resources(Water), sender = this, resource = Water))
  )
  
  val extractNutrients = ConditionalEmitter[SpendResource](
    condition = () => resources(Nutrient) < 100 && resources(Energy) > 5,
    eventGenerator = (_) => Some(SpendResource(
      targetId = this.id, 
      resource = Energy, 
      amount = 5, 
      sender = this,
      resultingEvent = ExtractResource(
        targetId = Entities.environment, 
        resource = Nutrient,
        amount = 10, // Or whatever amount you need
        sender = this
      ) 
    ))
  )

  val growthEmitter = TimedEmitter(
    frequency = 1000, 
    eventGenerator = time => Grow(this.id)
  )
  
  override def eventEmitters = super.eventEmitters :++ Seq(checkWater, extractNutrients)

  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case Grow(_) => {
      if (aboveGroundWeight < rootStrength) {
        if (leafSupport < leaves) {
          // growLeaf
        } else {
          // growStem
        }
      } else {
        // growRoot
      }
      Seq()
    }
    case ProduceSugar(_) => {
      ???
    }
    case event => super.eventHandlers(event)
  }
  
}

case class Grow(targetId: Long) extends Event
case class ProduceSugar(targetId: Long) extends Event