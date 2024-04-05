package model.Resources

sealed trait Resource {
  def name: String
}

sealed trait EnvironmentalResource extends Resource
sealed trait OrganismResource extends Resource
sealed trait AnimalResource extends OrganismResource
sealed trait PlantResource extends OrganismResource

// Shared Resources
case object Water extends EnvironmentalResource with OrganismResource { val name = "Water" }
case object Nutrient extends EnvironmentalResource with OrganismResource { val name = "Nutrient" }
case object Energy extends OrganismResource { val name = "Energy" }
case object Starch extends PlantResource { val name = "Starch" }
case object Sugar extends PlantResource with AnimalResource { val name = "Sugar" } 
case object Fat extends AnimalResource { val name = "Fat" }