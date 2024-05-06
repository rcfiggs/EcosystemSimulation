package model.events

sealed trait EventEmitter {
  def emit(time: Long): Seq[Event]
}

case class TimedEmitter(frequency: Int, eventGenerator: (Long) => Seq[Event]) extends EventEmitter {
  private var lastEmittedTime: Long = 0

  override def emit(time: Long): Seq[Event] = {
    if (time >= lastEmittedTime + frequency) {
      lastEmittedTime = time
      eventGenerator(time)
    } else {
      Seq()
    }
  }
}

case class ConditionalEmitter[E <: Event](condition: () => Boolean, eventGenerator: (Long) => Option[E]) extends EventEmitter {
  override def emit(time: Long): Seq[E] = {
    if (condition()) {
      eventGenerator(time) match 
        case Some(event) => Seq(event)
        case None => Seq()
    } else {
      Seq()
    }
  }
}

case class TestEmitter(events: Seq[Event] = Seq()) extends EventEmitter {
  override def emit(time: Long) = events
}