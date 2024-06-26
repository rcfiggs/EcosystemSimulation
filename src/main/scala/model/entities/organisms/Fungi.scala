package model.entities.organisms

import model.resources.{
  Resource, Water, Nutrient, Fat, Protein, Mycelium,
  ProduceMycelium, DecomposeFat, DecomposeProtein,
  Conversion,
}
import model.dna.{DNA, Extraction, Consumption, Capacity, Synthesis, InitialResource, MutationRate, Decomposition}
import model.entities.Organism
import model.dna.SurvivalRequirement
import model.events.{TimedEmitter, DecomposeResource}


case class Fungi(override val dna: DNA = Fungi.dna) extends Organism{
  override val targetable = (o: Organism) => o.isInstanceOf[PerishedOrganism]

  val decompose = TimedEmitter(
    frequency = 1000,
    eventGenerator = (time) => {
      target match {
        case Some(targetId) =>
          decompositionRate.toSeq.flatMap { 
            case (conv: Conversion, maxAmount) => {
              Seq(DecomposeResource(targetId = targetId, conversion = conv, amount = maxAmount))
            }
          }
        case None => Seq()
      }
    }
  )

  override val eventEmitters = super.eventEmitters ++ Seq(
    decompose,
  )
}

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
      SurvivalRequirement(Water) -> 5,
      SurvivalRequirement(Mycelium) -> 1,
      Decomposition(DecomposeFat) -> 1,
      Decomposition(DecomposeProtein) -> 1,
      MutationRate -> 3,
    )
  )
}