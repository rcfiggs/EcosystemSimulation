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