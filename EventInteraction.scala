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

  override val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: EndDay =>
      this.age += 1
      Seq(UpdateOrganismDisplay(this))
    case _ => Seq[Event]()
  }

  override def process(time: Int): Seq[Event] = Seq[Event]()

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
  val eventHandlers: PartialFunction[Event, Seq[Event]]

  def update(): Seq[Event] = {
    events.dequeueAll(_ => true).flatMap(eventHandlers)
  }
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

case class ButtonPressed(targetId: Long = Entities.endDayButton) extends Event

case class EndDay(organism: Organism) extends Event {
  override val targetId =  organism.id
}

class OrganismDisplay(dataList: ObservableBuffer[String], listView: ListView[String]) extends Entity {
  override val id = Entities.organismDisplay
  val organismMap: mutable.Map[Long, Int] = mutable.Map[Long,Int]()

  override val eventHandlers = {
    case event: UpdateOrganismDisplay =>
      val organism: Organism = event.organism
      if(organismMap.contains(organism.id)){
        dataList.update(organismMap(organism.id), organism.display) // replace name with data to be displayed
      } else {
        organismMap(organism.id) = dataList.size
        dataList.add(organism.display)
      }
      Seq[Event]() // return an empty sequence of events
    case _ => Seq[Event]() // handle other event types
  }

  override def process(time: Int): Seq[Event] = {
    Seq[Event]() // this method is not used anymore
  }
}

class EndDayButton(gameState: GameState, button: Button) extends Entity {
  override val id = Entities.endDayButton
  button.onAction = (_) => {
    this.events.enqueue(ButtonPressed())
  }

  override val eventHandlers: PartialFunction[Event, Seq[Event]] = {case _ => Seq[Event]()}

  override def update(): Seq[Event] = {
    while(events.nonEmpty){
      events.dequeue() match {
        case event: ButtonPressed => {
          for (organism <- gameState.entities.values) {
            if (organism.isInstanceOf[Organism]) {
              gameState.getEntity(Entities.organismDisplay).events.enqueue(UpdateOrganismDisplay(organism.asInstanceOf[Organism]))
            }
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

    val endDayButton = new Button("End Day")
    gameState.addEntity(EndDayButton(gameState, endDayButton))

    val vbox = new VBox(10) {
      children = Seq(listView, endDayButton)
    }

    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(vbox, 800, 600)
    }

    AnimationTimer((now: Long) => {
      gameState.processFrame(0)
    }).start()
  }
}