package ecoApp

trait Event {
  def targetId: Long
}

trait EventEmitter {
  def emit(time: Long): Seq[Event]
}