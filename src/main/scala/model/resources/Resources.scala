package model.Resources

sealed trait Resource{
  def name: String = this.getClass.getSimpleName
}

sealed trait NaturalResource extends Resource

sealed trait CompoundResource(val components: Map[Resource, Int]) extends Resource

// Simple and Natural Resources
case object Water extends NaturalResource
case object Nutrient extends NaturalResource
case object Energy extends Resource
case object Sunlight extends NaturalResource

// CompoundResource Resources
case object Starch extends Resource 
  with CompoundResource(components = Map[Resource, Int](Sugar -> 2, Energy -> 1))
case object Sugar extends Resource
  with CompoundResource(components = Map[Resource, Int](Water -> 1, Energy -> 5))
case object Fat extends Resource
  with CompoundResource(components = Map[Resource, Int](Sugar -> 5, Energy -> 4, Nutrient -> 4))
case object Cellulose extends Resource 
  with CompoundResource(components = Map[Resource, Int](Sugar -> 2, Energy -> 1))

sealed class Gatherer(
  val resource: NaturalResource,
  components: Map[Resource, Int]
) extends CompoundResource(
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
  with CompoundResource(Map(Cellulose -> 5))