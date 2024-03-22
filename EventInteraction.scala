import scalafx.Includes._
import scalafx.application.{JFXApp3, Platform}
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.{ListView, Button, ListCell, ChoiceBox}
import scalafx.scene.layout.VBox
import scalafx.animation.AnimationTimer

import scala.util.Random
import scala.collection.mutable

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.util.Callback

trait Organism extends Entity {
  val id: Long = Entities.newId
  val birthday: Int
  var energy: Int = 100
  var hydration: Int = 100
  var lastUpdateTime: Long = -1 // new field to track the last update time
  
  def process(time: Long): Seq[Event] = {
    if(lastUpdateTime == -1){
      lastUpdateTime = time
      Seq()
    } else {
      val timeDifference = time - lastUpdateTime // calculate time difference
      this.hydration -= hydrationLossRate(timeDifference) // use time difference to calculate hydration loss
      this.lastUpdateTime = time // update last update time
      Seq(UpdateOrganismDisplay(this))
    }
    
  }
  
  def hydrationLossRate(timeDifference: Long): Int
  
  def display: String = s"${this.getClass.getSimpleName}: Energy: $energy, Hydration: $hydration"
}

case class Plant(birthday: Int) extends Organism {
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: Sunshine =>
    this.energy += 10
    Seq(UpdateOrganismDisplay(this))
    case _ => Seq[Event]()
  }
  
  override def hydrationLossRate(timeDifference: Long): Int = {
    // lose 1 water per second
    (timeDifference / 1000).toInt
  }
}

case class Animal(birthday: Int) extends Organism {
  
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: Sunshine =>
    this.energy -= 10
    Seq(UpdateOrganismDisplay(this))
    case _ => Seq[Event]()
  }
  
  override def hydrationLossRate(time: Long): Int = 3
}

case class Fungi(birthday: Int) extends Organism {
  
  override def eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: Rain =>
    this.hydration += 10
    Seq(UpdateOrganismDisplay(this))
    case _ => Seq[Event]()
  }
  
  override def hydrationLossRate(time: Long): Int = 1
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
  def eventHandlers: PartialFunction[Event, Seq[Event]]
  
  def update(): Seq[Event] = {
    events.dequeueAll(_ => true).flatMap(eventHandlers)
  }
  def process(time: Long): Seq[Event]
}

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

class EntityManager(gameState: GameState) extends Entity {
  override val id = Entities.entityManager
  override val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case e: CreateOrganism => 
    // add the new organism to the game state
    gameState.addEntity(e.organism)
    // return a sequence of events that should be processed as a result of creating the organism
    Seq(UpdateOrganismDisplay(e.organism))
    case _ => Seq[Event]()
  }
  override def process(time: Long): Seq[Event] = {
    // the entity manager doesn't really process events, it just handles events that are related to creating new entities
    Seq()
  }
}

case class CreateOrganism(organism: Organism) extends Event{
  override val targetId = Entities.entityManager
}

case class UpdateOrganismDisplay(organism: Organism) extends Event {
  override val targetId = Entities.organismDisplay
}

case class ButtonPressed(targetId: Long) extends Event

case class EndDay(organism: Organism) extends Event {
  override val targetId =  organism.id
}

case class Sunshine(targetId: Long) extends Event
case class Rain(targetId: Long) extends Event

class OrganismDisplay(dataList: ObservableBuffer[Organism], listView: ListView[Organism]) extends Entity {
  override val id = Entities.organismDisplay
  val organismMap: mutable.Map[Long, Int] = mutable.Map[Long,Int]()
  listView.getSelectionModel.selectedIndexProperty.addListener(new ChangeListener[Number] {
    override def changed(observable: ObservableValue[? <: Number], oldIndex: Number, newIndex: Number): Unit = {
      println(s"Selected: ${listView.getSelectionModel.getSelectedItem.display}")
    }
  })
  listView.cellFactory = (cell: ListCell[Organism], organism: Organism) => cell.text = organism.display
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
  
  override def process(time: Long): Seq[Event] = {
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
  override def process(time: Long): Seq[Event] = {
    Seq[Event]()
  }
}

class CreateOrganismButton(button: Button, choiceBox: ChoiceBox[String]) extends Entity {
  override val id = Entities.createOrganismButton
  button.onAction = (_) => {
    this.events.enqueue(ButtonPressed(id))
  }
  
  override val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: ButtonPressed => {
      val organismType = choiceBox.getValue
      organismType match {
        case "Plant" => Seq(CreateOrganism(new Plant(birthday = 0)))
        case "Animal" => Seq(CreateOrganism(new Animal(birthday = 0)))
        case "Fungi" => Seq(CreateOrganism(new Fungi(birthday = 0)))
        case _ => Seq[Event]()
      }
    }
    case _ => Seq[Event]()
  }
  
  override def process(time: Long): Seq[Event] = {
    Seq[Event]()
  }
}

object SimpleApp extends JFXApp3 {
  val gameState: GameState = new GameState()
  
  override def start(): Unit = {
    val dataList = ObservableBuffer[Organism]()
    val listView = new ListView[Organism](dataList)
    gameState.addEntity(OrganismDisplay(dataList, listView))
    
    val firstPlant = new Plant(birthday = 0)
    gameState.addEntity(firstPlant)
    gameState.getEntity(Entities.organismDisplay).events.enqueue(UpdateOrganismDisplay(firstPlant))
    
    val firstAnimal = new Animal(birthday = 0)
    gameState.addEntity(firstAnimal)
    gameState.getEntity(Entities.organismDisplay).events.enqueue(UpdateOrganismDisplay(firstAnimal))
    
    val firstFungi = new Fungi(birthday = 0)
    gameState.addEntity(firstFungi)
    gameState.getEntity(Entities.organismDisplay).events.enqueue(UpdateOrganismDisplay(firstFungi))
    
    val endDayButton = new Button("End Day")
    gameState.addEntity(EndDayButton(gameState, endDayButton))
    
    val organismOptions = ObservableBuffer[String]("Plant", "Animal", "Fungi")
    val organismChoice = ChoiceBox[String](organismOptions)
    val createOrganismButton = new Button("Create Organism")
    gameState.addEntity(CreateOrganismButton(createOrganismButton, organismChoice))
    
    gameState.addEntity(EntityManager(gameState))
    
    val vbox = new VBox(10) {
      children = Seq(listView, endDayButton, organismChoice, createOrganismButton)
    }
    
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(vbox, 800, 600)
    }
    
    AnimationTimer((now: Long) => {
      val time = System.currentTimeMillis()
      gameState.processFrame(time)
    }).start()
  }
}