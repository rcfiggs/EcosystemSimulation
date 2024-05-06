package model.resources

sealed trait Resource{
  def name: String = this.getClass.getSimpleName
}

sealed trait NaturalResource extends Resource
//Natural Resources
case object Water extends NaturalResource
case object Sunlight extends NaturalResource
case object Nutrient extends NaturalResource

//Other Resources
case object Sugar extends Resource
case object Fat extends Resource
case object Protein extends Resource
case object Mycelium extends Resource

