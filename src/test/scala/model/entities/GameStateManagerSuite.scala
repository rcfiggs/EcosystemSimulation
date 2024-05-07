package model.entities

import model.GameState
import model.events.{CreateOrganism, Perished, FindTarget}
import model.entities.Organism
import model.resources.{Resource, Water}
import model.dna.DNA
import model.events.{
  UpdateOrganismDisplay, AddOrganismToDisplay, RemoveOrganismFromDisplay, TargetFound, TargetNotFound,
}
import model.entities.{TestOrganism}
import model.entities.organisms.{Plant, Fungi}

import org.scalatest.flatspec.AnyFlatSpec 
import org.scalatest.funsuite.AnyFunSuite

class GameStateManagerSuite extends AnyFunSuite {
  
  test("A CreateOrganism event should add the organism to the game state and return an AddOrganismToDisplay event") {
    val gameState = GameState()
    val manager = GameStateManager(gameState)
    val organism = TestOrganism()
    val event = CreateOrganism(() => organism)
    val actual = manager.eventHandlers(event)
    val expected = Seq(AddOrganismToDisplay(organism))
    assert(actual === expected)
  }
  
  test("A Perished event should remove the organism from the game state and return a RemoveOrganismFromDisplay event") {
    val gameState = GameState()
    val manager = GameStateManager(gameState)
    val organism = TestOrganism()
    gameState.entities += (organism.id -> organism)
    val event = Perished(organism)
    val actual = manager.eventHandlers(event)
    val expected = Seq(RemoveOrganismFromDisplay(organism.id))
    assert(actual === expected)
  }
  
  test("A FindTarget event should return a TargetFound event when a matching target is found") {
    val gameState = GameState()
    val manager = GameStateManager(gameState)
    val plant = Plant()
    val fungi = Fungi()
    gameState.entities += (plant.id -> plant)
    gameState.entities += (fungi.id -> fungi)
    val event = FindTarget({ case (_, o: Fungi) => o }, plant.id)
    val actual = manager.eventHandlers(event)
    val expected = Seq(TargetFound(plant.id, fungi.id))
    assert(actual === expected)
  }
  
  test("A FindTarget event should return a TargetNotFound event when no matching target is found") {
    val gameState = GameState()
    val manager = GameStateManager(gameState)
    val plant = Plant()
    gameState.entities += (plant.id -> plant)
    val event = FindTarget({ case (_, o: Fungi) => o }, plant.id)
    val actual = manager.eventHandlers(event)
    val expected = Seq(TargetNotFound(plant.id))
    assert(actual === expected)
  }
  
  test("An unhandled event should return an empty sequence") {
    val gameState = GameState()
    val manager = GameStateManager(gameState)
    val event = UpdateOrganismDisplay(TestOrganism())
    val actual = manager.eventHandlers(event)
    val expected = Seq.empty
    assert(actual === expected)
  }
}