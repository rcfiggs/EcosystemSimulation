package model.entities.organisms

import model.resources.{
  Resource, Water, Sugar, Nutrient, Protein, Fat,
  ProduceFat, ProduceProtein
}
import model.entities.Organism
import model.dna.{DNA, Extraction, Consumption, Capacity, Synthesis, InitialResource, MutationRate}
import model.events.Event
import scala.collection.mutable

case class Animal(override val dna: DNA = Animal.dna) extends Organism {
  override val targetable = (o: Organism) => o.isInstanceOf[Plant]
}

object Animal {
  val dna: DNA = DNA(
    properties = Map(
      Extraction(Water) -> 16,
      Consumption(Sugar) -> 8,
      Consumption(Nutrient) -> 1,
      Capacity(Water) -> 50,
      Capacity(Sugar) -> 50,
      Capacity(Nutrient) -> 50,
      Capacity(Protein) -> 50,
      Capacity(Fat) -> 50,
      Synthesis(ProduceFat) -> 8,
      Synthesis(ProduceProtein) -> 2,
      InitialResource(Water) -> 25,
      InitialResource(Sugar) -> 25,
      InitialResource(Nutrient) -> 25,
      InitialResource(Protein) -> 25,
      InitialResource(Fat) -> 25,
      MutationRate -> 3
    )
  )
}