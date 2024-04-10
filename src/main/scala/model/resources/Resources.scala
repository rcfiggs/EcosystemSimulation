package model.Resources

import ecoApp.Organism
import ecoApp.{Plant, Animal, Fungi}

sealed trait Resource {
  def name: String
}

sealed trait EnvironmentalResource extends Resource
sealed trait OrganismResource[+Organism] extends Resource

// Shared Resources
case object Water extends EnvironmentalResource, OrganismResource { val name = "Water" }
case object Nutrient extends EnvironmentalResource, OrganismResource { val name = "Nutrient" }
case object Energy extends OrganismResource { val name = "Energy" }
case object Starch extends OrganismResource[Plant] { val name = "Starch" }
case object Sugar extends OrganismResource[Animal | Plant] { val name = "Sugar" }
case object Fat extends OrganismResource[Animal] { val name = "Fat" }