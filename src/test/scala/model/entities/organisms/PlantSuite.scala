package model.entities.organisms

import org.scalatest.funsuite.AnyFunSuite


class PlantSuite extends AnyFunSuite {

  test("An Plant should have the correct DNA") {
    val plant = Plant()
    assert(plant.dna === Plant.dna)
  }

  test("An Plant should have the correct initial resources") {
    val plant = Plant()
    assert(plant.initialResources === Plant.initialResources)
  }
}