package ecoApp

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
  var nextId: Long = 7
  def newId: Long = {
    val id = nextId
    nextId += 1
    id
  }
}

class EntityManager(gameState: GameState) extends Entity {
  override val id = Entities.entityManager

  override val eventEmitters: Seq[EventEmitter] = Seq()

  override val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case e: CreateOrganism => 
    // add the new organism to the game state
    gameState.addEntity(e.organism)
    // return a sequence of events that should be processed as a result of creating the organism
    Seq(UpdateOrganismDisplay(e.organism))
    case Perished(organism) => {
      val perishedOrganism = PerishedOrganism(organism.id, 0)
      gameState.setEntity(perishedOrganism)
      Seq(UpdateOrganismDisplay(perishedOrganism))
    }
    case event: SearchForPlant =>
      val roll = scala.util.Random.nextInt(100)
      if (roll < 100) { // 20% chance of finding a plant
        val potentialPlants = gameState.entities.collect{ case (id, p: Plant) => p }.toVector
        if(potentialPlants.nonEmpty) {
          val plant = potentialPlants(scala.util.Random.nextInt(potentialPlants.size))
          Seq(FoundPlant(targetId = event.senderId, plantId = plant.id))
        }
        else Seq(NoPlantFound(event.senderId))
      } else {
        Seq(NoPlantFound(event.senderId))
      }
    case _ => Seq[Event]()
  }
}