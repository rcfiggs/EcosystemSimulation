package model.entities

import model.events.{
  Event, CreateOrganism, UpdateOrganismDisplay, Perished, 
  FindTarget, TargetFound, TargetNotFound,
  EventEmitter,
}
import model.GameState
import view.OrganismDisplay
import scala.collection.mutable

trait Entity {
  val id: Long
  def eventEmitters: Seq[EventEmitter]
  val events: mutable.Queue[Event] = mutable.Queue[Event]()
  def eventHandlers: PartialFunction[Event, Seq[Event]]
  
  def update(): Seq[Event] = {
    events.dequeueAll(_ => true).flatMap(event => eventHandlers.lift(event).getOrElse(Seq()))
  }
  def process(time: Long): Seq[Event] = {
    eventEmitters.flatMap(_.emit(time))
  }
}

object Entities {
  val entityManager = 1
  val organismDisplay = 2
  val endDayButton = 3
  val createOrganismButton = 4
  val environment = 5
  val environmentDisplay = 6
  val dnaDisplay = 7
  var nextId: Long = 8
  def newId: Long = {
    val id = nextId
    nextId += 1
    id
  }
}
