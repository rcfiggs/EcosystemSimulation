package model.entities.organisms

import model.resources.{
  Resource, Water, Sugar, Nutrient, Protein, Fat,
  ProduceFat, ProduceProtein
}
import model.entities.Organism
import model.dna.DNA
import model.events.Event
import scala.collection.mutable


case class Animal(override val dna: DNA = Animal.dna, override val initialResources: Map[Resource, Int] = Animal.initialResources) extends Organism {  
  override def eventEmitters = super.eventEmitters :++ Seq()
  
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = super.eventHandlers orElse {
    case _ => Seq()
  }
}

object Animal {
  val dna: DNA = DNA(
    intake = Map(
      Water -> 16,
    ),
    extraction = Map(
      Sugar -> 8,
      Nutrient -> 1,
    ),
    capacity = Map(
      Water -> 50,
      Sugar -> 50,
      Nutrient -> 50,
      Protein -> 50,
      Fat -> 50,
    ),
    synthesis = Map(
      ProduceFat -> 8,
      ProduceProtein -> 2,
    )
  )
  val initialResources: Map[Resource, Int] = Map(
  Water -> 25,
  Sugar -> 25,
  Nutrient -> 25,
  Protein -> 25,
  Fat -> 25,
  )
}