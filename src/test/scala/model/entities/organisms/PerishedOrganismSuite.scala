package model.entities.organisms

import org.scalatest.funsuite.AnyFunSuite
import _root_.model.resources.{Resource, Water, Sugar}
import _root_.model.events.{ExtractResource, IsPerished}
import _root_.model.dna.{DNA, Extraction, Consumption, Capacity, InitialResource}

class PerishedOrganismSuite extends AnyFunSuite {
  val dna: DNA = DNA(properties = Map())
  val resources = scala.collection.mutable.Map[Resource,Int]()
  test("A PerishedOrganism should have the correct DNA") {
    val perishedOrganism = PerishedOrganism(1, resources)
    assert(perishedOrganism.dna === dna)
  }

  test("A PerishedOrganism should have the correct initial resources") {
    val perishedOrganism = PerishedOrganism(1, resources)
    assert(perishedOrganism.resources === resources)
  }

  test("A PerishedOrganism should handle an ExtractResource event from an Animal correctly") {
    val animal = Animal()
    val perishedOrganism = PerishedOrganism(1, scala.collection.mutable.Map[Resource,Int]())
    val event = ExtractResource(targetId = 1, resource = Water, amount = 5, sender = animal)
    val actual = perishedOrganism.eventHandlers(event)
    val expected = Seq(IsPerished(animal.id, perishedOrganism))
    assert(actual === expected)
  }
}