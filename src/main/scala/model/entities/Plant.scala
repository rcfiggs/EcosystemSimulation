package ecoApp

import model.Resources._

case class Plant(birthday: Int) extends Organism {

  resources.addAll(List(
    Sunode -> 2,
    Watode -> 2,
    Nutrode -> 2,
    Sugase -> 1,
  ))
  val checkWater = ConditionalEmitter[ExtractResource](
  condition = () => (resources(Water) < 95),
  eventGenerator = (_) => Some(ExtractResource(targetId = Entities.environment, amount = 100 - resources(Water), sender = this, resource = Water))
  )
  def growLeaf: Event = {
    SpendResources(
      targetId = this.id,
      resources = Map[Resource, Int](Nutrient -> 10, Starch -> 25),
      resultingEvents = Seq(),
      sender = this,
    )
  }
  
  override def eventEmitters = super.eventEmitters :++ Seq()
  
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event => super.eventHandlers(event)
  }
  
}

case class Grow(targetId: Long) extends Event
case class ProduceSugar(targetId: Long) extends Event