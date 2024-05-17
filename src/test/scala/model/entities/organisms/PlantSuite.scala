package model.entities.organisms

import org.scalatest.funsuite.AnyFunSuite


class PlantSuite extends AnyFunSuite {

  test("An Plant should have the correct DNA") {
    val plant = Plant()
    assert(plant.dna === Plant.dna)
  }
}