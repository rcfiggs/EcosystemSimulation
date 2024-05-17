package model.entities.organisms

import org.scalatest.funsuite.AnyFunSuite

class FungiSuite extends AnyFunSuite {

  test("An Fungi should have the correct DNA") {
    val fungi = Fungi()
    assert(fungi.dna === Fungi.dna)
  }
}