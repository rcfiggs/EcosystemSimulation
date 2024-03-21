import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.ListView
import scalafx.scene.control.Button
import scalafx.scene.layout.VBox
import scalafx.animation.AnimationTimer
import scala.util.Random
import scala.collection.mutable

case class Organism(id: Long = Entities.newId, name: String, var age: Int) extends Entity {
  override def update(): Seq[Event] = Seq[Event]()
  override def process(time: Int): Seq[Event] = {
    // age += 1
    if (age % 10 == 0){
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
  def newId: Long = {
    val id = nextId
    nextId += 1
    id
  }
}

trait Event {
  def targetId: Long
}

trait Entity {
  val id: Long
  val events: mutable.Queue[Event] = mutable.Queue[Event]()

  def update(): Seq[Event]
  def process(time: Int): Seq[Event]
}

class GameState {
  val entities: mutable.Map[Long, Entity] = mutable.Map()

  def addEntity(entity: Entity): Unit = {
    entities.addOne(entity.id, entity)
  }

  def getEntity(id: Long): Entity = entities(id)

  def processFrame(time: Int): Unit = {
    for (entity <- entities.values) {
      val updateEvents = entity.update()
      updateEvents.foreach(e => entities(e.targetId).events.enqueue(e))

      val processEvents = entity.process(time)
      processEvents.foreach(e => entities(e.targetId).events.enqueue(e))
    }
  }
}

case class UpdateOrganismDisplay(organism: Organism) extends Event {
  override val targetId = Entities.organismDisplay
}

case class EndDay(organism: Organism) extends Event {
  override val targetId = Entities.endDayButton
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

class EndDayButton(gameState: GameState) extends Entity {
  override val id = Entities.endDayButton
  override def update(): Seq[Event] = Seq[Event]()
  override def process(time: Int): Seq[Event] = {
    for (organism <- gameState.entities.values) {
      if (organism.isInstanceOf[Organism]) {
        gameState.getEntity(Entities.organismDisplay).events.enqueue(UpdateOrganismDisplay(organism.asInstanceOf[Organism]))
      }
    }
    Seq[Event](EndDay(null))
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
    gameState.addEntity(EndDayButton(gameState))

    val vbox = new VBox(10) {
      children = Seq(listView, new Button("End Day") {
        onAction = (_) => {
          gameState.getEntity(Entities.endDayButton).process(0)
        }
      })
    }

    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(vbox, 800, 600)
    }

    AnimationTimer((now: Long) => {
      gameState.processFrame(0)
    }).start()
  }
}