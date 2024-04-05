
package ecoApp

import scalafx.Includes._
import scalafx.application.{JFXApp3, Platform}
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.{ListView, Button, ListCell, ChoiceBox}
import scalafx.scene.text.Text
import scalafx.scene.layout.{VBox, HBox}
import scalafx.animation.AnimationTimer
import model.entities.Environment
import ecoApp.Entities.environment
import view.EnvironmentDisplay
import view.UpdateEnviornmentDisplay


object SimpleApp extends JFXApp3 {
  def main(): Unit = {} 
  val gameState: GameState = new GameState()
  
  override def start(): Unit = {
    val dataList = ObservableBuffer[Organism]()
    val listView = new ListView[Organism](dataList)
    gameState.addEntity(OrganismDisplay(dataList, listView))
    val environment = Environment
    gameState.addEntity(environment)
    
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

    val environmentData = ObservableBuffer[String]()
    val environmentList = ListView[String](environmentData)
    gameState.addEntity(EnvironmentDisplay(environmentData, environmentList))
    gameState.getEntity(Entities.environmentDisplay).events.enqueue(UpdateEnviornmentDisplay("Water", environment.resources(Resource.Water).toString))
    gameState.getEntity(Entities.environmentDisplay).events.enqueue(UpdateEnviornmentDisplay("Nutrient", environment.resources(Resource.Nutrient).toString))

    val vbox = new VBox(10) {
      children = Seq(Text("Environment"), environmentList, Text("Organisms"), listView, endDayButton, organismChoice, createOrganismButton)
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