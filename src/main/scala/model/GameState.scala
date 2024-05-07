package model

import model.entities.{Entity}

import scala.collection.mutable
import scala.annotation.targetName

class GameState {
  val entities: mutable.Map[Long, Entity] = mutable.Map()
  val entitiesToRemove: mutable.Set[Long] = mutable.Set()

    var gameTime: Long = 0
  var lastFrameTime: Long = 0
  var paused: Boolean = false

  def addEntity(entity: Entity): Unit = {
    entities.addOne(entity.id, entity)
  }
  
  def getEntity(id: Long): Entity = entities(id)

  def setEntity(entity: Entity): Unit = entities(entity.id) = entity 

  def removeEntity(id: Long): Unit = entitiesToRemove.add(id)
  
  def processFrame(time: Long): Unit = {
    val deltaTime = time - lastFrameTime
    lastFrameTime = time

    if (!paused) {
      gameTime += deltaTime
    }

    for (entity <- entities.values) {
      val updateEvents = entity.update()
      updateEvents.foreach(e => {
        entities(e.targetId).events.enqueue(e)
      })
      
      if (!paused) {
        val processEvents = entity.process(gameTime)
        processEvents.foreach(e => {
          entities.get(e.targetId) match {
            case Some(entity) => entity.events.enqueue(e)
            case None => {
              println(s"Entity: ${e.targetId} Does not exist")
            }
          }
        })
      }
    }
    entitiesToRemove.foreach(id => entities -= id)
    entitiesToRemove.clear()
  }
}