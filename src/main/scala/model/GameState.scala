package ecoApp

import scala.collection.mutable

class GameState {
  val entities: mutable.Map[Long, Entity] = mutable.Map()
  
  def addEntity(entity: Entity): Unit = {
    entities.addOne(entity.id, entity)
  }
  
  def getEntity(id: Long): Entity = entities(id)
  
  def processFrame(time: Long): Unit = {
    for (entity <- entities.values) {
      val updateEvents = entity.update()
      updateEvents.foreach(e => entities(e.targetId).events.enqueue(e))
      
      val processEvents = entity.process(time)
      processEvents.foreach(e => entities(e.targetId).events.enqueue(e))
    }
  }
}