package model.entities.organisms

import org.scalatest.funsuite.AnyFunSuite
import _root_.model.resources.{Resource, Water, Sugar}
import _root_.model.events.{ExtractResource, IsPerished}
import _root_.model.dna.DNA

class PerishedOrganismSuite extends AnyFunSuite {

  val dna = DNA(
  intake = Map(Water -> 10),
  extraction = Map(Sugar -> 5),
  synthesis = Map(),
  capacity = Map(Water -> 50, Sugar -> 50)
)

  test("A PerishedOrganism should have the correct DNA") {
    val perishedOrganism = PerishedOrganism(1, dna)
    assert(perishedOrganism.dna === dna)
  }

  test("A PerishedOrganism should have the correct initial resources") {
    val initialResources: Map[Resource, Int] = Map(Water -> 10)
    val perishedOrganism = PerishedOrganism(1, dna, initialResources)
    assert(perishedOrganism.initialResources === initialResources)
  }

  test("A PerishedOrganism should handle an ExtractResource event from an Animal correctly") {
    val animal = Animal()
    val perishedOrganism = PerishedOrganism(1, dna)
    val event = ExtractResource(targetId = 1, resource = Water, amount = 5, sender = animal)
    val actual = perishedOrganism.eventHandlers(event)
    val expected = Seq(IsPerished(animal.id, perishedOrganism))
    assert(actual === expected)
  }
}