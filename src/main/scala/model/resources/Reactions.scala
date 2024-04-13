package model.Resources

sealed trait Reaction {
  def name: String
  def reactants: Map[Resource, Int]
  def products: Map[Resource, Int]
}

// sealed trait Metabolize(val name: String, reactant: CompoundResource) extends Reaction {
//   val reactants = Map(reactant -> 1, reactant.metabolicEnzyme -> 1)
//   val products = reactant.components + (reactant.metabolicEnzyme -> 1)
// }

sealed trait Synthesize()

case object Photosynthesis extends Reaction {
  override val name = "Photosynthesis"
  override val reactants = Map(Sunlight -> 1, Water -> 1)
  override val products =  Map(Sugar -> 1)
}

case object MetabolizeStarch extends Reaction {
  override val name = "Metabolize Starch"
  override val reactants = Map(Starch -> 1, Starchase -> 1)
  override val products = Starch.components + (Starchase -> 1)
}

case object SynthesizeStarch extends Reaction {
  override val name = "Synthesize Starch"
  override val reactants = Starch.components
  override val products = Map(Starch -> 1)
}