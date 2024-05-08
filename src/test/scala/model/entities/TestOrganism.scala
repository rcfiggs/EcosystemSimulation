package model.entities

import model.resources.{Resource, Water}
import model.dna.DNA

case class TestOrganism(
  override val id: Long = 0,
  override val dna: DNA = DNA(
    intake = Map(),
    extraction = Map(),
    synthesis = Map(),
    capacity = Map(),
    initialResources = Map(Water -> 1),
  ),
) extends Organism