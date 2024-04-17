package model.Resources

sealed trait Reaction {
  def name: String
  def reactants: Map[Resource, Int]
  def products: Map[Resource, Int]
}

sealed case class Gather(gatherer: Gatherer) extends Reaction {
  override val name = s"Gather ${gatherer.resource.getClass.getSimpleName}"
  override val reactants = Map(gatherer -> 1)
  override val products = Map(gatherer -> 1, gatherer.resource -> 1)
}

sealed class SimpleSynthesis(resource: CompoundResource) extends Reaction {
  override val name = s"Synthesize ${resource.getClass.getSimpleName}"
  override val reactants = resource.components
  override val products = Map(resource -> 1)
}

sealed case class SimpleMetabolism(resource: CompoundResource, enzyme: Enzyme) extends Reaction {
  override val name = s"Metabolize ${resource.getClass.getSimpleName}"
  override val reactants = Map(resource -> 1, enzyme -> 1)
  override val products = resource.components + (enzyme -> 1)
}

sealed class MetabolizeEnzyme(resource: CompoundResource) 
  extends SimpleMetabolism(resource = resource, enzyme = Enzase)

sealed class MetabolizeGatherer(resource: CompoundResource)
  extends SimpleMetabolism(resource = resource, enzyme = Gathase)

case object Photosynthesize extends Reaction {
  override val name = "Photosynthesize"
  override val reactants = Map(Sunlight -> 1, Water -> 1)
  override val products =  Map(Sugar -> 1)
}

object MetabolizeStarch extends SimpleMetabolism(Starch, Starchase) 
case object SynthesizeStarch extends SimpleSynthesis(Starch)

object MetabolizeSugar extends SimpleMetabolism(Sugar, Sugase)
case object SynthesizeSugar extends SimpleSynthesis(Sugar)

object MetabolizeFat extends SimpleMetabolism(Fat, Fatase)
case object SynthesizeFat extends SimpleSynthesis(Fat)

object MetabolizeCellulose extends SimpleMetabolism(Cellulose, Cellulase)
case object SynthesizeCellulose extends SimpleSynthesis(Cellulose)

object MetabolizeEnzase extends MetabolizeEnzyme(Enzase)
case object SynthesizeEnzase extends SimpleSynthesis(Enzase)

object MetabolizeStarchase extends MetabolizeEnzyme(Starchase)
case object SynthesizeStarchase extends SimpleSynthesis(Starchase)

object MetabolizeSugase extends MetabolizeEnzyme(Sugase)
case object SynthesizeSugase extends SimpleSynthesis(Sugase)

object MetabolizeFatase extends MetabolizeEnzyme(Fatase)
case object SynthesizeFatase extends SimpleSynthesis(Fatase)

object MetabolizeCellulase extends MetabolizeEnzyme(Cellulase)
case object SynthesizeCellulase extends SimpleSynthesis(Cellulase)

object MetabolizeSunode extends MetabolizeGatherer(Sunode)
case object SynthesizeSunode extends SimpleSynthesis(Sunode)
object GatherSunlight extends Gather(Sunode)

object MetabolizeWatode extends MetabolizeGatherer(Watode)
case object SynthesizeWatode extends SimpleSynthesis(Watode)
object GatherWater extends Gather(Watode)

object MetabolizeNutrode extends MetabolizeGatherer(Nutrode)
case object SynthesizeNutrode extends SimpleSynthesis(Nutrode)
object GatherNutrient extends Gather(Nutrode)