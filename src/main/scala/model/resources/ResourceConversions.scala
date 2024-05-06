package model.resources

sealed trait Conversion {
  def inputs: Map[Resource, Int]
  def outputs: Map[Resource, Int]

  def apply(inputs: Map[Resource, Int]): Option[Map[Resource, Int]] = {
    if (inputs forall { case (resource, quantity) => this.inputs(resource) <= quantity }) {
      Some(outputs)
    } else {
      None
    }
  }
}

case object ProduceSugar extends Conversion {
  override def inputs: Map[Resource, Int] = Map(Water -> 2, Sunlight -> 1)
  override def outputs: Map[Resource, Int] = Map(Sugar -> 1)
}

case object ExtractNutrientFromMycelium extends Conversion {
  override def inputs: Map[Resource, Int] = Map(Mycelium -> 1, Sunlight -> 1)
  override def outputs: Map[Resource, Int] = Map(Nutrient -> 2)
}

case object ProduceFat extends Conversion {
  override def inputs: Map[Resource, Int] = Map(Water -> 2, Sugar -> 1)
  override def outputs: Map[Resource, Int] = Map(Fat -> 1)
}

case object ProduceProtein extends Conversion {
  override def inputs: Map[Resource, Int] = Map(Sugar -> 1, Nutrient -> 1)
  override def outputs: Map[Resource, Int] = Map(Protein -> 1)
}

case object ProduceMycelium extends Conversion {
  override def inputs: Map[Resource, Int] = Map(Protein -> 1, Fat -> 1, Nutrient -> 1)
  override def outputs: Map[Resource, Int] = Map(Mycelium -> 1)
}

case class TestConversion(
  override val inputs: Map[Resource, Int] = Map(),
  override val outputs: Map[Resource, Int] = Map(),
) extends Conversion