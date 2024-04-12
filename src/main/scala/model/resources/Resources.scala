package model.Resources

sealed trait Resource{
  def name: String
}

sealed trait SimpleResource(val name: String) extends Resource

sealed trait CompoundResource(override val name: String, components: Map[Resource, Int], enzyme: Enzyme) extends Resource

class Enzyme(name: String, components: Map[Resource, Int]) extends CompoundResource(name = name, components = components, enzyme = Enzase)

case object Enzase extends Enzyme(name = "Enzase", 
components = Map[Resource, Int](Fat -> 5, Starch -> 5, Energy -> 5, Nutrient -> 5))

case object Starase extends Enzyme(name = "Starase",
components = Map[Resource, Int](Sugar -> 2, Energy -> 1, Nutrient -> 1))

case object Sugase extends Enzyme(name = "Sugase",
components = Map[Resource, Int](Energy -> 1, Nutrient -> 2))

case object Fatase extends Enzyme(name = "Fatase" ,
components = Map[Resource, Int](Starch -> 2, Energy -> 1, Nutrient -> 3))

case object Synthase extends Enzyme(name = "Synthase",
components = Map[Resource,Int](Fat -> 10, Starch -> 10, Energy -> 20))

case object Gathase extends Enzyme(name = "Gathase", 
components = Map[Resource, Int](Fat -> 10, Starch -> 10, Energy -> 20))

// Simple Resources
case object Water extends SimpleResource("Water")
case object Nutrient extends SimpleResource("Nutrient")
case object Energy extends SimpleResource("Energy")

// Compound Resources
case object Starch extends CompoundResource(name = "Starch",
components = Map[Resource, Int](Sugar -> 2, Energy -> 1),
enzyme = Starase)
case object Sugar extends CompoundResource(name = "Sugar",
components = Map[Resource, Int](Water -> 1, Energy -> 5),
enzyme = Sugase)
case object Fat extends CompoundResource(name = "Fat",
components = Map[Resource, Int](Sugar -> 5, Energy -> 4, Nutrient -> 4),
enzyme = Fatase)

// Resource Acquirers
sealed trait Acquirer {
  def resource: Resource
}

class Synthesizer(name: String, override val resource: CompoundResource, components: Map[Resource, Int]) 
extends Acquirer with CompoundResource(name = name, components = components, enzyme = Synthase)

case object Suganor extends Synthesizer(name = "Suganor", resource = Sugar, 
components = Map[Resource, Int](Energy -> 2, Nutrient -> 2))

case object Starchanor extends Synthesizer (name = "Starchanor", resource = Starch,
components = Map[Resource, Int](Energy -> 2, Sugar -> 2, Nutrient -> 2))

case object Fatanor extends Synthesizer (name = "Fatanor", resource = Fat,
components = Map[Resource, Int](Energy -> 5, Starch -> 3, Nutrient -> 4))

case object Staraseanor extends Synthesizer (name = "Staraseanor", resource = Starase,
components = Map[Resource, Int](Energy -> 3, Sugar -> 3, Nutrient -> 4))

class Gatherer(
  name: String, 
  override val resource: SimpleResource, 
  components: Map[Resource, Int]
) extends Acquirer with CompoundResource(
  name = name, 
  components = components, 
  enzyme = Gathase
)

case object Enode extends Gatherer(
  name = "Enode",
  resource = Energy,
  components =  Map[Resource, Int](Starch -> 3, Nutrient -> 3),
)

case object Watode extends Gatherer (
  name = "Watode",
  resource = Water,
  components = Map[Resource, Int](Starch -> 2, Nutrient -> 2),
)

case object Nutrode extends Gatherer (
  name = "Nutrode",
  resource = Nutrient,
  components = Map[Resource, Int](Starch -> 3, Energy -> 1, Water -> 1)
)
