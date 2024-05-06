package model.entities

import model.events.{Rainfall, Flood, UpdateEnviornmentDisplay, ExtractResource, ResourceGain}
import model.resources.Water
import org.scalatest.flatspec.AnyFlatSpec 
import org.scalatest.funsuite.AnyFunSuite

class EnviornmentSuite extends AnyFunSuite {

  test("Rainfall event should be emitted after 30 seconds"){
    val actual = Environment.process(30000)
    val matches: Boolean = actual match {
      case Seq(Rainfall(time, amount)) 
        if(time == 30000 && (amount >= 1500 && amount <= 6000)) => true
      case _ => false
    }
    assert(matches)
  }

  test("Receiving a Rainfall event should result in a Flood when the Enviroment is past it's maximum"){
    Environment.events.enqueue(Rainfall(0, 1000)) 
    val actual = Environment.update()
    val matches: Boolean = actual match {
      case Seq(Flood(0, 1000), UpdateEnviornmentDisplay(Water, 10000)) => true
      case _ => false
    }
    assert(matches)
  }

  test("Receving a Rainfall event while there is space left should result in an UpdateEnviornment event with the update resource value"){
    Environment.resources.update(Water, 0)
    Environment.events.enqueue(Rainfall(0, 1000))
    val actual = Environment.update()
    val matches: Boolean = actual match {
      case Seq(UpdateEnviornmentDisplay(Water, 1000)) => true
      case _ => false
    }
    assert(matches && Environment.resources(Water) == 1000)
  }

  test("Extract Resource when requested amount is less than or equal to the current amount should return the amount requested"){
    Environment.resources.update(Water, 10000)
    Environment.events.enqueue(ExtractResource(0, Water, 5000, TestOrganism()))
    val actual = Environment.update()
    val matches: Boolean = actual match {
      case Seq(
        UpdateEnviornmentDisplay(Water, 5000),
        ResourceGain(0, Water, 5000),
      ) => true
      case _ => false
    }
    assert(matches && Environment.resources(Water) == 5000)
  }

  test("Extract Resource when requested amount is greater than the current amount should return the amount available"){
    Environment.resources.update(Water, 10000)
    Environment.events.enqueue(ExtractResource(0, Water, 20000, TestOrganism()))
    val actual = Environment.update()
    val matches: Boolean = actual match {
      case Seq(
        UpdateEnviornmentDisplay(Water, 0),
        ResourceGain(0, Water, 10000),
      ) => true
      case _ => false
    }
    assert(matches && Environment.resources(Water) == 0)
  }

}