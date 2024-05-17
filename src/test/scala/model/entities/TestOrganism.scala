package model.entities

import model.resources.{Resource, Water}
import model.dna.{DNA, InitialResource}

case class TestOrganism(
  override val id: Long = 0,
  override val dna: DNA = DNA(properties = Map(
    InitialResource(Water) -> 1
  ))
) extends Organism