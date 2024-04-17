package ecoApp

import model.Resources._

sealed trait PlantComponent extends OrganismComponent
case object Leaf extends PlantComponent
case object Root extends PlantComponent
case object Stem extends PlantComponent
case object Seed extends PlantComponent

case class Plant(birthday: Int, var roots: Int = 1, var leaves: Int = 2, var stems: Int = 1) extends Organism {

  def aboveGroundWeight: Int = stems * 2 + leaves
  def rootStrength: Int = roots * 2
  def leafSupport: Int = stems * 2
  resources.addAll(List(
    Sunode -> 2,
    Watode -> 2,
    Nutrode -> 2,
    Sugase -> 1,
  ))
  reactions ++= Seq(
    Photosynthesize,
    SynthesizeStarch,
    SynthesizeSugar,
    MetabolizeSugar,
    SynthesizeSugase,
    SynthesizeCellulose,
    SynthesizeSunode,
    SynthesizeWatode,
    SynthesizeNutrode,
    GatherSunlight,
    GatherWater,
    GatherNutrient,
  )
  val checkWater = ConditionalEmitter[ExtractResource](
    condition = () => (resources(Water) < 95),
    eventGenerator = (_) => Some(ExtractResource(targetId = Entities.environment, amount = 100 - resources(Water), sender = this, resource = Water))
  )
  
  // val extractNutrients = ConditionalEmitter[SpendResource](
  //   condition = () => resources(Nutrient) < 100 && resources(Energy) > 5,
  //   eventGenerator = (_) => Some(SpendResource(
  //     targetId = this.id, 
  //     resource = Energy, 
  //     amount = 5, 
  //     sender = this,
  //     resultingEvent = ExtractResource(
  //       targetId = Entities.environment, 
  //       resource = Nutrient,
  //       amount = 10, // Or whatever amount you need
  //       sender = this
  //     ) 
  //   ))
  // )

  val growthEmitter = TimedEmitter(
    frequency = 1000, 
    eventGenerator = time => Grow(this.id)
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
    // case Grow(_) => {
    //   if (aboveGroundWeight < rootStrength) {
    //     if (leafSupport < leaves) {
    //       // growLeaf
    //     } else {
    //       // growStem
    //     }
    //   } else {
    //     // growRoot
    //   }
    //   Seq()
    // }
    case InsufficientResources(targetId, insufficientResources, failedEvents) => {
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