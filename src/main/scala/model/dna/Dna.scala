package model.dna

import model.resources.{Resource, Conversion}

sealed trait DNAEntry {
  def amount: Int
  def toString: String
}

case class IntakeEntry(resource: Resource, amount: Int) extends DNAEntry {
  override def toString: String = s"Intake $resource: $amount"
}

case class ExtractionEntry(resource: Resource, amount: Int) extends DNAEntry {
  override def toString: String = s"Extraction $resource: $amount"
}

case class SynthesisEntry(conversion: Conversion, amount: Int) extends DNAEntry {
  override def toString: String = s"Synthesis $conversion: $amount"
}

case class CapacityEntry(resource: Resource, amount: Int) extends DNAEntry {
  override def toString: String = s"Capacity $resource: $amount"
}

case class DNA (
val intake: Map[Resource, Int],
val extraction: Map[Resource, Int],
val synthesis: Map[Conversion, Int],
val capacity: Map[Resource, Int],
){
  val increaseFactor = 1.2

  val toEntries: Seq[DNAEntry] = {
    intake.map { case (resource, amount) => IntakeEntry(resource, amount) } ++
    extraction.map { case (resource, amount) => ExtractionEntry(resource, amount) } ++
    synthesis.map { case (conversion, amount) => SynthesisEntry(conversion, amount) } ++
    capacity.map { case (resource, amount) => CapacityEntry(resource, amount) }
  }.toSeq
  def withModifiedProperty(dnaEntry: DNAEntry): DNA = {
    dnaEntry match {
      case IntakeEntry(resource, _) =>
      val newAmount = math.ceil(intake.getOrElse(resource, 0) * increaseFactor).toInt
      copy(intake.updated(resource, newAmount), extraction, synthesis, capacity)
      case ExtractionEntry(resource, _) =>
      val newAmount = math.ceil(extraction.getOrElse(resource, 0) * increaseFactor).toInt
      copy(intake, extraction.updated(resource, newAmount), synthesis, capacity)
      case SynthesisEntry(conversion, _) =>
      val newAmount = math.ceil(synthesis.getOrElse(conversion, 0) * increaseFactor).toInt
      copy(intake, extraction, synthesis.updated(conversion, newAmount), capacity)
      case CapacityEntry(resource, _) =>
      val newAmount = math.ceil(capacity.getOrElse(resource, 0) * increaseFactor).toInt
      copy(intake, extraction, synthesis, capacity.updated(resource, newAmount))
    }
  }
}