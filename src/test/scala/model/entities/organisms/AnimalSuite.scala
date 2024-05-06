package model.entities.organisms

import org.scalatest.funsuite.AnyFunSuite

class AnimalSuite extends AnyFunSuite {

  test("An Animal should have the correct DNA") {
    val animal = Animal()
    assert(animal.dna === Animal.dna)
  }

  test("An Animal should have the correct initial resources") {
    val animal = Animal()
    assert(animal.initialResources === Animal.initialResources)
  }
}