package model.Resources

sealed trait Resource {
  def name: String
}

// Shared Resources
case object Water extends Resource { val name = "Water" }
case object Nutrient extends Resource { val name = "Nutrient" }
case object Energy extends Resource { val name = "Energy" }
case object Starch extends Resource { val name = "Starch" }
case object Sugar extends Resource { val name = "Sugar" }
case object Fat extends Resource { val name = "Fat" }