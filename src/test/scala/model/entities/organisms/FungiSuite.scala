package model.entities.organisms

import org.scalatest.funsuite.AnyFunSuite

class FungiSuite extends AnyFunSuite {

  test("An Fungi should have the correct DNA") {
    val fungi = Fungi()
    assert(fungi.dna === Fungi.dna)
  }

  test("An Fungi should have the correct initial resources") {
    val fungi = Fungi()
    assert(fungi.initialResources === Fungi.initialResources)
  }
}