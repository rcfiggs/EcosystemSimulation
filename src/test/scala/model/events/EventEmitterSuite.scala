package model.events

import org.scalatest.funsuite.AnyFunSuite

class EventEmitterSuite extends AnyFunSuite {

  test("TimedEmitter should emit events at the correct frequency") {
    var count = 0
    val emitter = TimedEmitter(10, _ => { count += 1; Seq(TestEvent()) })
    assert(emitter.emit(0) === Seq()) // Not enough time has passed
    assert(count === 0)
    assert(emitter.emit(10) === Seq(TestEvent())) // Enough time has passed, event is emitted
    assert(count === 1)
    assert(emitter.emit(15) === Seq()) // Not enough time has passed again
    assert(count === 1)
    assert(emitter.emit(20) === Seq(TestEvent())) // Enough time has passed again, event is emitted
    assert(count === 2)
  }

  test("ConditionalEmitter should emit events when the condition is met") {
    var count = 0
    val emitter = ConditionalEmitter(() => true, _ => Some(TestEvent()))
    assert(emitter.emit(0) === Seq(TestEvent()))
    assert(count === 0)
    val emitter2 = ConditionalEmitter(() => false, _ => Some(TestEvent()))
    assert(emitter2.emit(0) === Seq())
    assert(count === 0)
  }

  test("ConditionalEmitter should not emit events when the generator returns None") {
    var count = 0
    val emitter = ConditionalEmitter(() => true, _ => None)
    assert(emitter.emit(0) === Seq())
    assert(count === 0)
  }
}