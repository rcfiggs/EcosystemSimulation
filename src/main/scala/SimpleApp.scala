package ecoApp

import scalafx.Includes._
import scalafx.application.{JFXApp3, Platform}
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.{ListView, Button, ListCell, ChoiceBox}
import scalafx.scene.layout.VBox
import scalafx.animation.AnimationTimer

object SimpleApp extends JFXApp3 {
  def main(): Unit = {} 
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