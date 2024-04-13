package model.Resources

sealed trait Resource{
  def name: String
}

sealed trait NaturalResource extends Resource
sealed trait SimpleResource(val name: String) extends Resource
sealed trait CompoundResource(
  override val name: String, 
  val components: Map[Resource, Int], 
  // val metabolicEnzyme: Option[Enzyme] = None,
  // val syntheticEnzyme: Option[Enzyme] = None,
) extends Resource

// Simple and Natural Resources
case object Water extends NaturalResource with SimpleResource("Water")
case object Nutrient extends NaturalResource with SimpleResource("Nutrient")
case object Energy extends SimpleResource("Energy")
case object Sunlight extends NaturalResource with SimpleResource("Sunlight")

// Compound Resources
case object Starch extends CompoundResource(name = "Starch",
components = Map[Resource, Int](Sugar -> 2, Energy -> 1))
case object Sugar extends CompoundResource(name = "Sugar",
components = Map[Resource, Int](Water -> 1, Energy -> 5))
case object Fat extends CompoundResource(name = "Fat",
components = Map[Resource, Int](Sugar -> 5, Energy -> 4, Nutrient -> 4))

// Enzymes
class Enzyme(name: String, components: Map[Resource, Int]) extends CompoundResource(name = name, components = components/*, enzyme = Enzase*/)

case object Enzase extends Enzyme(name = "Enzase", 
components = Map[Resource, Int](Fat -> 5, Starch -> 5, Energy -> 5, Nutrient -> 5))

case object Starchase extends Enzyme(name = "Starchase",
components = Map[Resource, Int](Sugar -> 2, Energy -> 1, Nutrient -> 1))

case object Sugase extends Enzyme(name = "Sugase",
components = Map[Resource, Int](Energy -> 1, Nutrient -> 2))

case object Fatase extends Enzyme(name = "Fatase" ,
components = Map[Resource, Int](Starch -> 2, Energy -> 1, Nutrient -> 3))

case object Synthase extends Enzyme(name = "Synthase",
components = Map[Resource,Int](Fat -> 10, Starch -> 10, Energy -> 20))

case object Gathase extends Enzyme(name = "Gathase", 
components = Map[Resource, Int](Fat -> 10, Starch -> 10, Energy -> 20))

case object Cellulase extends Enzyme(name = "Cellulase",
components = Map[Resource, Int](Nutrient -> 5, Energy -> 3))

// Resource Acquirers
sealed trait Acquirer {
  def resource: Resource
}

// class Synthesizer(name: String, override val resource: CompoundResource, components: Map[Resource, Int]) 
// extends Acquirer with CompoundResource(name = name, components = components, enzyme = Synthase)

// case object Suganor extends Synthesizer(name = "Suganor", resource = Sugar, 
// components = Map[Resource, Int](Energy -> 2, Nutrient -> 2))

// case object Starchanor extends Synthesizer (name = "Starchanor", resource = Starch,
// components = Map[Resource, Int](Energy -> 2, Sugar -> 2, Nutrient -> 2))

// case object Fatanor extends Synthesizer (name = "Fatanor", resource = Fat,
// components = Map[Resource, Int](Energy -> 5, Starch -> 3, Nutrient -> 4))

// case object Staraseanor extends Synthesizer (name = "Staraseanor", resource = Starase,
// components = Map[Resource, Int](Energy -> 3, Sugar -> 3, Nutrient -> 4))

class Gatherer(
  name: String, 
  override val resource: NaturalResource, 
  components: Map[Resource, Int]
) extends Acquirer with CompoundResource(
  name = name, 
  components = components, 
  // metabolicEnzyme = Some(Gathase)
)

case object Sunode extends Gatherer(
  name = "Sunode",
  resource = Sunlight,
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

// Structural Resources
sealed trait Structure

case object Cellulose extends Structure with CompoundResource(
  name = "Cellulose",
  components = Map[Resource, Int](Sugar -> 2, Energy -> 1),
  // metabolicEnzyme = Some(Cellulase)
)