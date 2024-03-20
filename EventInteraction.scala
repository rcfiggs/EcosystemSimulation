import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.ListView
import scalafx.scene.layout.VBox
import scalafx.animation.AnimationTimer
import scala.util.Random
import scala.collection.mutable

case class Organism(name: String, age: Int) extends Entity {
  override def update(): Seq[Event] = Seq[Event]
  override def process(time: Int): Seq[Event] = Seq[Event]
}

object Entities {
  val organismDisplay = 1
  val endDayButton = 2
  val thingList = 3
}

// Define the Event trait
trait Event {
  def targetId: Long
}

// Define the Entity trait
trait Entity {
  val id: Int
  val events: mutable.Queue[Event]

  def update(): Seq[Event]
  def process(time: Int): Seq[Event]
}

// Define the GameState class
class GameState {
  val entities: mutable.Map[Long, Entity] = mutable.Map()
  var nextId: Long = 1

  // Generate a unique ID
  def newId(): Long = {
    val id = nextId
    nextId += 1
    id
  }

  def addEntity(id: Long, entity: Entity): Unit = {
    entities.addOne(id, entity)
  }

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

    // 3. Process all outputed events
    // for (entity <- entities.values) {
    //   while (entity.events.nonEmpty) {
    //     val event = entity.events.dequeue()
    //     // Process the event here
    //   }
    // }
  }
}

 case class UpdateOrganismDisplay(organism: Organism) extends  Event

class OrganismDisplay(dataList: ObservableBuffer[String], listView: Listview[String]) extends Entity {
  val organismMap: mutable.Map[Long, Int] = mutable.Map[Long,Int]()
  override def update(): Seq[Event] = {
    while(events.nonEmpty){
      events.dequeue() match {
        case organism: UpdateOrganismDisplay => {
          if(!organismMap.contains(organism.id)){
            organismMap.add(organism.id, dataList.size)
          }
          dataList.update(organismMap(event.organismId), organism.name) // replace name with data to be displayed
        }
        case _ => ???
      }
    }
  }

  override def process(time: Int): Seq[Event] = {
    Seq[Event]
  }

}

object SimpleApp extends JFXApp3 {
  val gameState: GameState = new GameState()
  override def start(): Unit = {
    val dataList = ObservableBuffer[String]()
    val listView = new ListView[String](dataList)
    gameState.addEntity(Entities.organismDisplay, OrganismDisplay(dataList, listView))

    
    val vbox = new VBox(10) {
      children = Seq(listView)
    }
    
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(vbox, 800, 600)
    }
    
    AnimationTimer((now: Long) => {
      // Update the data list
      val updatedList = list.updated(0, Random.nextInt)
      val diff = updatedList.zip(list).collect { case (newItem, oldItem) if newItem != oldItem => (newItem, oldItem) }
      diff.foreach { case (newItem, oldItem) =>
        dataList.update(dataList.indexOf(oldItem.toString), newItem.toString)
      }
      list = updatedList
    }).start()
  }
}