package model.entities.organisms

import model.entities.{Organism}
import model.dna.DNA
import model.resources.{
  Resource, Water, Sunlight, Mycelium, Nutrient, Sugar,
  ProduceSugar, ExtractNutrientFromMycelium,
}
import model.events.Event
import scala.collection.mutable
import scala.collection.immutable

case class Plant(val dna: DNA = Plant.dna, val initialResources: Map[Resource, Int] = Plant.initialResources) extends Organism {
  
  override def eventEmitters = super.eventEmitters :++ Seq()
  
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event => super.eventHandlers(event)
  }
  
}

object Plant {
  val dna = DNA(
    intake = Map(
    Water -> 32,
    Sunlight -> 16,
    ),
    extraction = Map(
      Mycelium -> 1,
    ),
    capacity = Map(
    Water -> 100,
    Sunlight -> 100,
    Mycelium -> 100,
    Nutrient -> 100,
    Sugar -> 100,
    ),
    synthesis = Map(
    ProduceSugar -> 16,
    ExtractNutrientFromMycelium -> 2,
    )
  )
  val initialResources: Map[Resource, Int] = Map(
  Water -> 40,
  Sunlight -> 40,
  Mycelium -> 40,
  Nutrient -> 40,
  Sugar -> 40,
  )
}