package model.entities

import model.GameState
import model.events.{
  Event, CreateOrganism, UpdateOrganismDisplay, Perished, AddOrganismToDisplay, RemoveOrganismFromDisplay,
  FindTarget, TargetFound, TargetNotFound, Pause, Play,
  EventEmitter,
  eventToSeq,
}
import model.entities.organisms.{PerishedOrganism}
import view.OrganismDisplay

case class GameStateManager(gameState: GameState) extends Entity {
  override val id = Entities.gameStateManager
  
  override val eventEmitters: Seq[EventEmitter] = Seq()
  
  override val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case Pause => {
      gameState.paused = true
      Seq()
    }
    case Play => {
      gameState.paused = false
      Seq()
    }
    case CreateOrganism(newOrganism) => {
      val organism = newOrganism()
      gameState.addEntity(organism)
      AddOrganismToDisplay(organism)
    }
    case Perished(organism) => {
      val perished = PerishedOrganism(organism.id, organism.resources)
      gameState.setEntity(perished)
      Seq(
        RemoveOrganismFromDisplay(organism.id),
        AddOrganismToDisplay(perished),
      )
    }
    case FindTarget(targetable, senderId: Long) => {
      val roll = scala.util.Random.nextInt(100)
      if (roll < 100) { // 100% chance of finding a target
        val potentialTargets = gameState.entities.values.collect { case o: Organism if targetable(o) => o }.toVector
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

  def processFrame(time: Long): Unit = {
    gameState.processFrame(time)
  }
}

