package model.entities.organisms

import model.resources.{
  Resource, Water, Nutrient, Fat, Protein, Mycelium,
  ProduceMycelium
}
import model.dna.DNA
import model.entities.Organism


case class Fungi(override val dna: DNA = Fungi.dna, override val initialResources: Map[Resource, Int] = Fungi.initialResources) extends Organism

object Fungi{
  val dna: DNA = DNA(
    intake = Map(
      Water -> 8,
      Nutrient -> 1,
    ),
    extraction = Map(
      Fat -> 4,
      Protein -> 1,
    ),
    capacity = Map(
      Water -> 25,
      Fat -> 25,
      Protein -> 25,
      Mycelium -> 25,
      Nutrient -> 5,
    ),
    synthesis = Map(
      ProduceMycelium -> 2,
    ),
  )
  val initialResources: Map[Resource, Int] = Map(
  Water -> 10,
  Protein -> 10,
  Fat -> 10,
  Mycelium -> 10,
  Nutrient -> 5,
  )
}