package model.entities

import model.events.{Event, EventEmitter}

case class TestEntity(
  override val id: Long = 0,
  override val eventEmitters: Seq[EventEmitter] = Seq(),
  override val eventHandlers: PartialFunction[Event, Seq[Event]] = {case _ => Seq()}

) extends Entity