package model.entities

import model.resources._
import model.dna.{DNA}

import org.scalatest.flatspec.AnyFlatSpec 
import org.scalatest.funsuite.AnyFunSuite
import model.events.{ConditionalEmitter, TestEvent, eventToSeq}
import model.entities.Entity

class EntitySuite extends AnyFunSuite {

  test("A call to Update with no events in the queue should return an empty sequence"){
    val entity = TestEntity()
    val actual = entity.update()
    val expected = Seq()
    assert(actual === expected)
  }

  test("A call to update with a single TestEvent should return a sequence with a single TestEvent"){
    val entity = TestEntity(eventHandlers = {
      case TestEvent(_) => Seq(TestEvent())
    })
    entity.events.enqueue(TestEvent())
    val actual = entity.update()
    val expected = Seq((TestEvent()))
    assert(actual === expected)
  }

  test("A call to process should return a sequence containing a single TestEvent"){
    val entity = TestEntity(eventEmitters = Seq(
      ConditionalEmitter(
        condition = () => true, 
        eventGenerator = (_) => Some(TestEvent())
      )
    ))
    val actual = entity.process(0)
    val expected = Seq(TestEvent())
    assert(actual === expected)
  }

  test("A call to newId should give a different Id each time"){
    val id1 = Entities.newId
    val id2 = Entities.newId
    assert(id1 !== id2)
  }
  
}
