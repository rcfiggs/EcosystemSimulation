package model.entities

import model.GameState
import model.events.{
  Event, CreateOrganism, UpdateOrganismDisplay, Perished, AddOrganismToDisplay, RemoveOrganismFromDisplay,
  FindTarget, TargetFound, TargetNotFound,
  EventEmitter,
  eventToSeq,
}
import view.OrganismDisplay

case class EntityManager(gameState: GameState) extends Entity {
  override val id = Entities.entityManager
  
  override val eventEmitters: Seq[EventEmitter] = Seq()
  
  override val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case CreateOrganism(newOrganism) => {
      val organism = newOrganism()
      gameState.addEntity(organism)
      AddOrganismToDisplay(organism)
    }
    case Perished(organism) => {
      gameState.removeEntity(organism.id)
      RemoveOrganismFromDisplay(organism.id)
    }
    case FindTarget(pf, senderId: Long) => {
      val roll = scala.util.Random.nextInt(100)
      if (roll < 100) { // 100% chance of finding a plant
        val potentialTargets = gameState.entities.collect(pf).toVector
        if(potentialTargets.nonEmpty) {
          val target = potentialTargets(scala.util.Random.nextInt(potentialTargets.size))
          TargetFound(targetId = senderId, foundId = target.id)
        }
        else TargetNotFound(senderId)
      } else {
        TargetNotFound(senderId)
      }
    }
    case _ => Seq[Event]()
  } 
}