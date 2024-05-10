package model.entities.organisms

import model.entities.{Organism}
import model.dna.{DNA, Extraction, Consumption, Capacity, Synthesis, InitialResource}
import model.resources.{
  Resource, Water, Sunlight, Mycelium, Nutrient, Sugar,
  ProduceSugar, ExtractNutrientFromMycelium,
}
import model.events.Event
import scala.collection.mutable
import scala.collection.immutable

case class Plant(val dna: DNA = Plant.dna) extends Organism {
  
  override def eventEmitters = super.eventEmitters :++ Seq()
  
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event => super.eventHandlers(event)
  }
  
}

object Plant {
  val dna = DNA(
    properties = Map(
      Extraction(Water) -> 32,
      Extraction(Sunlight) -> 16,
      Consumption(Mycelium) -> 1,
      Capacity(Water) -> 100,
      Capacity(Sunlight) -> 100,
      Capacity(Mycelium) -> 100,
      Capacity(Nutrient) -> 100,
      Capacity(Sugar) -> 100,
      Synthesis(ProduceSugar) -> 16,
      Synthesis(ExtractNutrientFromMycelium) -> 2,
      InitialResource(Water) -> 40,
      InitialResource(Sunlight) -> 40,
      InitialResource(Mycelium) -> 40,
      InitialResource(Nutrient) -> 40,
      InitialResource(Sugar) -> 40,
    )
  )
}