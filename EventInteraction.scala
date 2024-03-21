import scalafx.Includes._
import scalafx.application.{JFXApp3, Platform}
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.{ListView, Button, ListCell}
import scalafx.scene.layout.VBox
import scalafx.animation.AnimationTimer

import scala.util.Random
import scala.collection.mutable

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.util.Callback

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
  val entityManager = 1
  val organismDisplay = 2
  val endDayButton = 3
  val createOrganismButton = 4
  var nextId: Long = 5
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

class EntityManager(gameState: GameState) extends Entity {
  override val id = Entities.entityManager
  override val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case e: CreateOrganism => 
    // create a new organism entity
    val newId = Entities.newId
    val newOrganism = new Organism(newId, newId.toString, 0)
    // add the new organism to the game state
    gameState.addEntity(newOrganism)
    // return a sequence of events that should be processed as a result of creating the organism
    Seq(UpdateOrganismDisplay(newOrganism))
  }
  
  override def process(time: Int): Seq[Event] = {
    // the entity manager doesn't really process events, it just handles events that are related to creating new entities
    Seq()
  }
}

class CreateOrganism extends Event{
  override val targetId = Entities.entityManager
}

case class UpdateOrganismDisplay(organism: Organism) extends Event {
  override val targetId = Entities.organismDisplay
}

case class ButtonPressed(targetId: Long) extends Event

case class EndDay(organism: Organism) extends Event {
  override val targetId =  organism.id
}

class OrganismDisplay(dataList: ObservableBuffer[Organism], listView: ListView[Organism]) extends Entity {
  override val id = Entities.organismDisplay
  val organismMap: mutable.Map[Long, Int] = mutable.Map[Long,Int]()
  listView.getSelectionModel.selectedIndexProperty.addListener(new ChangeListener[Number] {
    override def changed(observable: ObservableValue[? <: Number], oldIndex: Number, newIndex: Number): Unit = {
      println(s"Selected: ${listView.getSelectionModel.getSelectedItem.display}")
    }
  })
  listView.cellFactory = (listView: ListView[Organism]) => {
    val cell = new ListCell[Organism] {
      def updateItem(item: Organism, empty: Boolean): Unit = {
        if (empty || item == null) {
          text = ""
        } else {
          text = item.display
        }
      }
    }
    cell
  }
  override val eventHandlers = {
    case event: UpdateOrganismDisplay =>
    val organism: Organism = event.organism
    if(organismMap.contains(organism.id)){
      dataList.update(organismMap(organism.id), organism) // replace name with data to be displayed
    } else {
      organismMap(organism.id) = dataList.size
      dataList.add(organism)
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
    this.events.enqueue(ButtonPressed(id))
  }
  
  override val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: ButtonPressed => {
      gameState.entities.values.collect {
        case organism: Organism => EndDay(organism)
      }.toSeq
    }
    case _ => Seq[Event]()
  }
  override def process(time: Int): Seq[Event] = {
    Seq[Event]()
  }
}

class CreateOrganismButton(button: Button) extends Entity {
  override val id = Entities.createOrganismButton
  button.onAction = (_) => {
    this.events.enqueue(ButtonPressed(id))
  }
  
  override val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: ButtonPressed => {
      Seq(CreateOrganism())
    }
    case _ => Seq[Event]()
  }
  
  override def process(time: Int): Seq[Event] = {
    Seq[Event]()
  }
}  

object SimpleApp extends JFXApp3 {
  val gameState: GameState = new GameState()
  
  override def start(): Unit = {
    val dataList = ObservableBuffer[Organism]()
    val listView = new ListView[Organism](dataList)
    gameState.addEntity(OrganismDisplay(dataList, listView))
    val first: Organism = Organism(name = "first!", age = 0)
    gameState.addEntity(first)
    gameState.getEntity(Entities.organismDisplay).events.enqueue(UpdateOrganismDisplay(first))
    
    val endDayButton = new Button("End Day")
    gameState.addEntity(EndDayButton(gameState, endDayButton))
    
    val createOrganismButton = new Button("Create Organism")
    gameState.addEntity(CreateOrganismButton(createOrganismButton))
    
    gameState.addEntity(EntityManager(gameState))
    
    val vbox = new VBox(10) {
      children = Seq(listView, endDayButton, createOrganismButton)
    }
    
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(vbox, 800, 600)
    }
    
    AnimationTimer((now: Long) => {
      gameState.processFrame(0)
    }).start()
  }
}