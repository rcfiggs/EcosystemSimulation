package model.Resources

sealed trait Organ {
  def components: Map[Acquirer | Structure, Int]
}


sealed trait Tissue {
  def capacity: Map[Resource, Int]
}

case object Xylem extends Tissue {
  val capacity = Map[Resource, Int](Water -> 5)
}

case object Phloem extends Tissue {
  val capacity = Map[Resource, Int](Sugar -> 3, Nutrient -> 3)
}