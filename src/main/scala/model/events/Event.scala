package ecoApp

trait Event {
  def targetId: Long
}

trait EventEmitter {
  def emit(time: Long): Seq[Event]
}

case class TimedEmitter[E <: Event](frequency: Int, eventGenerator: (Long) => E) extends EventEmitter {
  private var lastEmittedTime: Long = 0

  override def emit(time: Long): Seq[Event] = {
    if (time >= lastEmittedTime + frequency) {
      lastEmittedTime = time
      Seq(eventGenerator(time))
    } else {
      Seq()
    }
  }
}

case class ConditionEmitter[E <: Event](condition: () => Boolean, eventGenerator: (Long) => E) extends EventEmitter {
  override def emit(time: Long): Seq[E] = {
    if (condition()) {
      Seq(eventGenerator(time))
    } else {
      Seq()
    }
  }
}