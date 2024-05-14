package model.dna

import model.resources.{Resource, Conversion}

import scala.language.implicitConversions

sealed trait DNAProperty

case class Extraction(resource: Resource) extends DNAProperty{
  override def toString: String = s"Extraction: $resource"
}
case class Consumption(resource: Resource) extends DNAProperty{
  override def toString: String = s"Consumption: $resource"
}
case class Synthesis(conversion: Conversion) extends DNAProperty{
  override def toString: String = s"Synthesis: ${conversion.inputs} -> ${conversion.outputs}"
}
case class Capacity(resource: Resource) extends DNAProperty{
  override def toString: String = s"Capacity: $resource"
}
case class InitialResource(resource: Resource) extends DNAProperty{
  override def toString: String = s"Initial Resource: $resource"
}
case class SurvivalRequirement(resource: Resource) extends DNAProperty {
  override def toString: String = s"Survival Requirement: $resource"
}
case object MutationRate extends DNAProperty{
  override def toString: String = "Mutation Rate"
}
case class Decomposition(conversion: Conversion) extends DNAProperty{
  override def toString: String = s"Decomposition: ${conversion.inputs} -> ${conversion.outputs}"
}



case class DNA (
val properties: Map[DNAProperty, Int]
){
  
  val increaseFactor = 1.2
  
  val toEntries: Seq[DNAEntry] = properties.map { case (property, value) => DNAEntry(property, value) }.toSeq
  
  def withMutation(mutation: DNAMutation): DNA = {
    val newValue = (properties.getOrElse(mutation.property, 0) * (1 + mutation.modifier)).toInt
    copy(properties = properties.updated(mutation.property, newValue))
  }
  
  def getRandomMutations(numMutations: Int): Seq[DNAMutation] = {
    val allProperties = properties.keys.toSeq
    val random = new scala.util.Random
    val cappedNumMutations = math.min(numMutations, allProperties.size)
    random.shuffle(allProperties).take(cappedNumMutations).map { property =>
      val modifier = random.nextDouble() * 2 - 1 // Random modifier between -1 and 1
      DNAMutation(property, modifier)
    }
  }

  val randomMutations = getRandomMutations(properties.getOrElse(MutationRate, 1))
  val initialResources: Map[Resource, Int] = properties.collect { case (InitialResource(resource), amount) => resource -> amount }
  val intakeRate: Map[Resource, Int] = properties.collect { case (Extraction(resource), amount) => resource -> amount }
  val extractionRate: Map[Resource, Int] = properties.collect { case (Consumption(resource), amount) => resource -> amount }
  val synthesisRate: Map[Conversion, Int] = properties.collect { case (Synthesis(conversion), amount) => conversion -> amount }
  val resourceCapacities: Map[Resource, Int] = properties.collect { case (Capacity(resource), amount) => resource -> amount }
  val survivalRequirements: Map[Resource, Int] = properties.collect { case (SurvivalRequirement(resource), amount) => resource -> amount }
}

case class DNAEntry(property: DNAProperty, value: Int) {
  override def toString: String = s"$property ($value)"
}

case class DNAMutation(property: DNAProperty, modifier: Double) {
  override def toString: String = s"$property (${modifier * 100}%)"
}
