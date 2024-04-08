package model.Resources

import ecoApp.Organism
import ecoApp.Plant
import ecoApp.Animal

sealed trait Resource {
  def name: String
}

sealed trait EnvironmentalResource extends Resource
sealed trait OrganismResource[O <: Organism[O]] extends Resource
// sealed trait AnimalResource extends OrganismResource[Animal]
// sealed trait PlantResource extends OrganismResource[Plant]

// Shared Resources
case object Water extends EnvironmentalResource with OrganismResource { val name = "Water" }
case object Nutrient extends EnvironmentalResource with OrganismResource { val name = "Nutrient" }
case object Energy extends OrganismResource[Any] { val name = "Energy" }
case object Starch extends OrganismResource[Plant] { val name = "Starch" }
case object Sugar extends OrganismResource[Plant | Animal] { val name = "Sugar" } 
case object Fat extends OrganismResource[Animal] { val name = "Fat" }