package ecoApp

import model.Resources._

case class Plant(birthday: Int) extends Organism {

  resources.addAll(List(
  Sunode -> 2,
  Watode -> 2,
  Nutrode -> 2,
  ))
  val checkWater = ConditionalEmitter[ExtractResource](
  condition = () => (resources(Water) < 95),
  eventGenerator = (_) => Some(ExtractResource(targetId = Entities.environment, amount = 100 - resources(Water), sender = this, resource = Water))
  )
  def synthesizeResource(resource: CompoundResource) = {
    SpendResources(
    targetId = this.id, 
    resources = resource.components, 
    resultingEvents = Seq(ResourceGain(
    targetId = this.id,
    resource = resource,
    amount = 1,
    )),
    )
  }
  def extractNaturalResource(resource: NaturalResource) = {
    resource match
    case Water => SpendResources(
    targetId = this.id,
    resources = Map(Watode -> 1), 
    resultingEvents = Seq(
    ResourceGain(targetId = this.id, resource = Watode, amount = 1),
    ResourceGain(targetId = this.id, resource = Water, amount = 1)
    ))
    case Nutrient => SpendResources(
    targetId = this.id,
    resources = Map(Nutrode -> 1), 
    resultingEvents = Seq(
    ResourceGain(targetId = this.id, resource = Nutrode, amount = 1),
    ResourceGain(targetId = this.id, resource = Nutrient, amount = 1)
    ))
    case Sunlight => SpendResources(
    targetId = this.id,
    resources = Map(Sunode -> 1), 
    resultingEvents = Seq(
    ResourceGain(targetId = this.id, resource = Sunode, amount = 1),
    ResourceGain(targetId = this.id, resource = Sunlight, amount = 1)
    ))
  }
  def metabolizeSugar = {
    SpendResources(
    targetId = this.id,
    resources = Map(Sugar -> 1),
    resultingEvents = Sugar.components.toSeq.map {
      (resource, amount) => ResourceGain(targetId = this.id, resource = resource, amount = amount)
    },
    )
  }
  override val acquire = {
    case resource: CompoundResource => resource match 
      case Sugar => Seq(SpendResources(
      targetId = this.id,
      resources = Map(Water -> 2, Sunlight -> 1),
      resultingEvents = Seq(ResourceGain(targetId = this.id, resource = Sugar, amount = 1)),
      ))
      case _ => Seq(synthesizeResource(resource))
    case resource: NaturalResource => Seq(extractNaturalResource(resource))
    case Energy => Seq(metabolizeSugar)
  }
  
  def growLeaf: Event = {
    SpendResources(
    targetId = this.id,
    resources = Map[Resource, Int](Nutrient -> 10, Starch -> 25),
    resultingEvents = Seq(),
    )
  }
  
  override def eventEmitters = super.eventEmitters :++ Seq()
  
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event => super.eventHandlers(event)
  }
  
}

case class Grow(targetId: Long) extends Event
case class ProduceSugar(targetId: Long) extends Event