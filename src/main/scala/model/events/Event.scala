package ecoApp

trait Event {
  def targetId: Long
}

trait EventEmitter {
  def emit(time: Long): Seq[Event]
}

trait TimedEmitter[E <: Event](frequency: Int, eventGenerator: (Long) => E) extends EventEmitter {
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