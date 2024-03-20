import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.ListView
import scalafx.scene.layout.VBox
import scalafx.animation.AnimationTimer
import scala.util.Random
import scala.collection.mutable

case class Organism(id: Long = Entities.newId, name: String, var age: Int) extends Entity {
  override def update(): Seq[Event] = Seq[Event]()
  override def process(time: Int): Seq[Event] = {
    age += 1
    if (age % 100 == 0){
      Seq[Event](UpdateOrganismDisplay(this))
    } else {
      Seq[Event]()
    }
    
  }

  def display: String = s"$name: $age"
}

object Entities {
  val organismDisplay = 1
  val endDayButton = 2
  val thingList = 3
  var nextId: Long = 4
    // Generate a unique ID
  def newId: Long = {
    val id = nextId
    nextId += 1
    id
  }
}

// Define the Event trait
trait Event {
  def targetId: Long
}

// Define the Entity trait
trait Entity {
  val id: Long
  val events: mutable.Queue[Event] = mutable.Queue[Event]()

  def update(): Seq[Event]
  def process(time: Int): Seq[Event]
}

// Define the GameState class
class GameState {
  val entities: mutable.Map[Long, Entity] = mutable.Map()

  def addEntity(entity: Entity): Unit = {
    entities.addOne(entity.id, entity)
  }

  def getEntity(id: Long): Entity = entities(id)

  // Process a frame
  def processFrame(time: Int): Unit = {
    for (entity <- entities.values) {
      // 1. Call update on every entity
      val updateEvents = entity.update()
      updateEvents.foreach(e => entities(e.targetId).events.enqueue(e))

      // 2. Call process on each entity
      val processEvents = entity.process(time)
      processEvents.foreach(e => entities(e.targetId).events.enqueue(e))
    }
  }
}

 case class UpdateOrganismDisplay(organism: Organism) extends Event{
  override val targetId = Entities.organismDisplay
 }

class OrganismDisplay(dataList: ObservableBuffer[String], listView: ListView[String]) extends Entity {
  override val id = Entities.organismDisplay
  val organismMap: mutable.Map[Long, Int] = mutable.Map[Long,Int]()
  override def update(): Seq[Event] = {
    while(events.nonEmpty){
      events.dequeue() match {
        case event: UpdateOrganismDisplay => {
          val organism: Organism = event.organism
          if(organismMap.contains(organism.id)){
            dataList.update(organismMap(organism.id), organism.display) // replace name with data to be displayed
          } else {
            organismMap(organism.id) = dataList.size
            dataList.add(organism.display)
          }
        }
        case _ => ???
      }
    }
    Seq[Event]()
  }

  override def process(time: Int): Seq[Event] = {
    Seq[Event]()
  }

}

object SimpleApp extends JFXApp3 {
  val gameState: GameState = new GameState()

  override def start(): Unit = {
    val dataList = ObservableBuffer[String]()
    val listView = new ListView[String](dataList)
    gameState.addEntity(OrganismDisplay(dataList, listView))
    val first: Organism = Organism(name = "first!", age = 0)
    gameState.addEntity(first)
    gameState.getEntity(Entities.organismDisplay).events.enqueue(UpdateOrganismDisplay(first))

    
    val vbox = new VBox(10) {
      children = Seq(listView)
    }
    
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(vbox, 800, 600)
    }
    
    AnimationTimer((now: Long) => {
      gameState.processFrame(0)
    }).start()
  }
}