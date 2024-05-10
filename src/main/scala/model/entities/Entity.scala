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
  val gameStateManager = 1
  val organismDisplay = 2
  val pauseButton = 3
  val playButton = 4
  val createOrganismButton = 5
  val environment = 6
  val environmentDisplay = 7
  val dnaDisplay = 8
  val createOrganismWindow = 9
  var nextId: Long = 10
  def newId: Long = {
    val id = nextId
    nextId += 1
    id
  }
}
