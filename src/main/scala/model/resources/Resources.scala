package model.Resources

import ecoApp.Organism
import ecoApp.{Plant, Animal, Fungi}

sealed trait Resource {
  def name: String
}

sealed trait EnvironmentalResource extends Resource

type AllOrganisms = Animal | Plant | Fungi
sealed trait OrganismResource[O <: Organism[O]] extends Resource


// Shared Resources
trait Water[O <: Organism[O]]() extends EnvironmentalResource with OrganismResource[O] { val name = "Water" }
trait Nutrient[O <: Organism[O]]() extends EnvironmentalResource with OrganismResource[O] { val name = "Nutrient" }
trait Energy[O <: Organism[O]]() extends OrganismResource[O] { val name = "Energy" }
case object Starch extends OrganismResource[Plant] { val name = "Starch" }
case object Sugar extends OrganismResource[Plant | Animal] { val name = "Sugar" }
case object Fat extends OrganismResource[Animal] { val name = "Fat" }