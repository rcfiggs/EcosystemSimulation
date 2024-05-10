package model.entities.organisms

import model.resources.{
  Resource, Water, Nutrient, Fat, Protein, Mycelium,
  ProduceMycelium
}
import model.dna.{DNA, Extraction, Consumption, Capacity, Synthesis, InitialResource}
import model.entities.Organism


case class Fungi(override val dna: DNA = Fungi.dna) extends Organism

object Fungi{
  val dna: DNA = DNA(
    properties = Map(
      Extraction(Water) -> 8,
      Extraction(Nutrient) -> 1,
      Consumption(Fat) -> 4,
      Consumption(Protein) -> 1,
      Capacity(Water) -> 25,
      Capacity(Fat) -> 25,
      Capacity(Protein) -> 25,
      Capacity(Mycelium) -> 25,
      Capacity(Nutrient) -> 5,
      Synthesis(ProduceMycelium) -> 2,
      InitialResource(Water) -> 10,
      InitialResource(Protein) -> 10,
      InitialResource(Fat) -> 10,
      InitialResource(Mycelium) -> 10,
      InitialResource(Nutrient) -> 5,
    )
  )
}