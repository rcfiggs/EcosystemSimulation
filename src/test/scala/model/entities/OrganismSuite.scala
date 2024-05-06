package model.entities

import model.events.{
  Rainfall, Flood, UpdateEnviornmentDisplay, ExtractResource, ResourceGain,
  ResourceLost, UpdateOrganismDisplay, SpendResources, InsufficientResources,
  TargetFound, TargetNotFound, IsPerished, Reproduce, CreateOrganism,
}
import model.resources.Water
import model.dna.IntakeEntry

import org.scalatest.funsuite.AnyFunSuite

class OrganismSuite extends AnyFunSuite {

  test("A ResourceLost event should update the organism's resources and return an UpdateOrganismDisplay event") {
    val organism = TestOrganism(initialResources = Map(Water -> 10))
    val event = ResourceLost(targetId = organism.id, resource = Water, amount = 5)
    val actual = organism.eventHandlers(event)
    val expected = Seq(UpdateOrganismDisplay(organism))
    assert(actual === expected)
  }

  test("A ResourceGain event should update the organism's resources and return an UpdateOrganismDisplay event") {
    val organism = TestOrganism(initialResources = Map(Water -> 10))
    val event = ResourceGain(targetId = organism.id, resource = Water, amount = 5)
    val actual = organism.eventHandlers(event)
    val expected = Seq(UpdateOrganismDisplay(organism))
    assert(actual === expected)
  }

  test("An ExtractResource event should update the organism's resources and return a ResourceLost event for the target, and a ResrouceGain event for the organism") {
    val organism = TestOrganism(initialResources = Map(Water -> 10))
    val event = ExtractResource(targetId = 0, resource = Water, amount = 5, sender = organism)
    val actual = organism.eventHandlers(event)
    val expected = Seq(ResourceLost(targetId = 0, resource = Water, amount = 5), ResourceGain(0, Water, 5))
    assert(actual === expected)
  }

  test("A SpendResources event should update the organism's resources and return a ResourceLost event") {
    val organism = TestOrganism(initialResources = Map(Water -> 10))
    val event = SpendResources(targetId = organism.id, resources = Map(Water -> 5), resultingEvents = Seq())
    val actual = organism.eventHandlers(event)
    val expected = Seq(
      ResourceLost(targetId = organism.id, resource = Water, amount = 5),
      UpdateOrganismDisplay(organism))
    assert(actual === expected)
  }

  test("An InsufficientResources event should not update the organism's resources and return an empty sequence") {
    val organism = TestOrganism(initialResources = Map(Water -> 10))
    val event = InsufficientResources(targetId = organism.id, resources = Map(Water -> 15))
    val actual = organism.eventHandlers(event)
    val expected = Seq.empty
    assert(actual === expected)
  }

  test("A TargetFound event should update the organism's target and return an empty sequence") {
    val organism = TestOrganism()
    val event = TargetFound(targetId = 0, foundId = 1)
    val actual = organism.eventHandlers(event)
    val expected = Seq.empty
    assert(organism.target === Some(1))
    assert(actual === expected)
  }

  test("A TargetNotFound event should update the organism's target and return an empty sequence") {
    val organism = TestOrganism()
    val event= TargetNotFound(targetId = 0)
    val actual = organism.eventHandlers(event)
    val expected = Seq.empty
    assert(organism.target === None)
    assert(actual === expected)
  }

  // To be implemented Functionality
  // test("An IsPerished event should update the organism's target and return an empty sequence") {
  //   val organism = TestOrganism()
  //   val event = IsPerished(targetId = 0, organism = organism)
  //   val actual = organism.eventHandlers(event)
  //   val expected = Seq.empty
  //   assert(organism.target === None)
  //   assert(actual === expected)
  // }.ignore

  test("A Reproduce event should create a new organism and return a CreateOrganism event") {
    val organism = TestOrganism()
    val event = Reproduce(targetId = 0, dnaEntry = IntakeEntry(Water, 0))
    val actual = organism.eventHandlers(event)
    val matches = actual match {
      case Seq(
        UpdateOrganismDisplay(organism), 
        CreateOrganism(of)
      ) => true
      case _ => false
    }
    assert(matches)
  }
}