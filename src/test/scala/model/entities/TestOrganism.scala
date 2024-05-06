package model.entities

import model.resources.{Resource, Water}
import model.dna.DNA

case class TestOrganism(
  override val id: Long = 0,
  override val initialResources: Map[Resource, Int] = Map(Water -> 1),
  override val dna: DNA = DNA(intake = Map(), extraction = Map(), synthesis = Map(), capacity = Map()),
) extends Organism