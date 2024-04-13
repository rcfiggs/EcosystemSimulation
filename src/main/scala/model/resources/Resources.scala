package model.Resources

sealed trait Resource{
  def name: String = this.getClass.getSimpleName
}

sealed trait NaturalResource extends Resource

sealed trait Compound(val components: Map[Resource, Int])

// Simple and Natural Resources
case object Water extends NaturalResource
case object Nutrient extends NaturalResource
case object Energy extends Resource
case object Sunlight extends NaturalResource

// Compound Resources
case object Starch extends Resource 
  with Compound(components = Map[Resource, Int](Sugar -> 2, Energy -> 1))
case object Sugar extends Resource
  with Compound(components = Map[Resource, Int](Water -> 1, Energy -> 5))
case object Fat extends Resource
  with Compound(components = Map[Resource, Int](Sugar -> 5, Energy -> 4, Nutrient -> 4))
case object Cellulose extends Resource 
  with Compound(components = Map[Resource, Int](Sugar -> 2, Energy -> 1))

// Enzymes
class Enzyme(components: Map[Resource, Int]) extends Resource 
  with Compound(components = components)

case object Enzase extends Enzyme(
  components = Map[Resource, Int](Fat -> 5, Starch -> 5, Energy -> 5, Nutrient -> 5))

case object Starchase extends Enzyme(
components = Map[Resource, Int](Sugar -> 2, Energy -> 1, Nutrient -> 1))

case object Sugase extends Enzyme(
components = Map[Resource, Int](Energy -> 1, Nutrient -> 2))

case object Fatase extends Enzyme(
components = Map[Resource, Int](Starch -> 2, Energy -> 1, Nutrient -> 3))

case object Synthase extends Enzyme(
components = Map[Resource,Int](Fat -> 10, Starch -> 10, Energy -> 20))

case object Gathase extends Enzyme( 
components = Map[Resource, Int](Fat -> 10, Starch -> 10, Energy -> 20))

case object Cellulase extends Enzyme(
components = Map[Resource, Int](Nutrient -> 5, Energy -> 3))

sealed class Gatherer(
  name: String = this.getClass.getSimpleName,
  resource: NaturalResource,
  components: Map[Resource, Int]
) extends Compound(
  components = components, 
)

case object Sunode extends Gatherer(
  resource = Sunlight,
  components =  Map[Resource, Int](Cellulose -> 3, Nutrient -> 3),
)

case object Watode extends Gatherer (
  resource = Water,
  components = Map[Resource, Int](Cellulose -> 2, Nutrient -> 2),
)

case object Nutrode extends Gatherer (
  resource = Nutrient,
  components = Map[Resource, Int](Cellulose -> 3, Energy -> 1, Water -> 1)
)

// Structural Resources
sealed trait Structure

case object Xylem extends Structure 
  with Compound(Map(Cellulose -> 5))