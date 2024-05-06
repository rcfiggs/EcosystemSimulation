
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
import view.EnvironmentDisplay
import scalafx.scene.layout.Priority
import scalafx.geometry.Insets
import view.{DNADisplay, OrganismDisplay, CreateOrganismButton}
import model.GameState
import model.entities.{Entity, Entities, EntityManager, Organism}
import model.entities.organisms.{Plant, Animal, Fungi}
import model.dna.{DNAEntry}
import model.events.{Event, UpdateOrganismDisplay, UpdateEnviornmentDisplay}
import model.resources.{Water, Nutrient, Sunlight}


object SimpleApp extends JFXApp3 {
  def main(): Unit = {} 
  val gameState: GameState = new GameState()
  
  override def start(): Unit = {

    val organismList = ObservableBuffer[Organism]()
    val organismView = new ListView[Organism](organismList)
    gameState.addEntity(OrganismDisplay(organismList, organismView))

    val actionList = ObservableBuffer[DNAEntry]()
    val actionView = new ListView[DNAEntry](actionList)
    gameState.addEntity(DNADisplay(actionList, actionView))

    val environment = Environment
    gameState.addEntity(environment)

    val organismsHBox = new HBox(10) {
      children = Seq(
        new VBox(10) {
          children = Seq(
            Text("Organisms"),
            organismView
          )
          hgrow = Priority.Always
        },
        new VBox(10) {
          children = Seq(
            Text("Organism DNA"),
            actionView
          )
          hgrow = Priority.Always
        }
      )
    }
    
    val firstAnimal = new Animal()
    gameState.addEntity(firstAnimal)
    gameState.getEntity(Entities.organismDisplay).events.enqueue(UpdateOrganismDisplay(firstAnimal))
    
    val firstFungi = new Fungi()
    gameState.addEntity(firstFungi)
    gameState.getEntity(Entities.organismDisplay).events.enqueue(UpdateOrganismDisplay(firstFungi))
    
    val firstPlant = new Plant()
    gameState.addEntity(firstPlant)
    gameState.getEntity(Entities.organismDisplay).events.enqueue(UpdateOrganismDisplay(firstPlant))
    
    val organismOptions = ObservableBuffer[String]("Plant", "Animal", "Fungi")
    val createOrganismButton = new Button("Create Organism")
    gameState.addEntity(CreateOrganismButton(createOrganismButton))
    
    gameState.addEntity(EntityManager(gameState))

    val environmentData = ObservableBuffer[String]()
    val environmentList = ListView[String](environmentData)
    gameState.addEntity(EnvironmentDisplay(environmentData, environmentList))
    gameState.getEntity(Entities.environmentDisplay).events.enqueue(UpdateEnviornmentDisplay(Water, environment.resources(Water)))
    gameState.getEntity(Entities.environmentDisplay).events.enqueue(UpdateEnviornmentDisplay(Nutrient, environment.resources(Nutrient)))
    gameState.getEntity(Entities.environmentDisplay).events.enqueue(UpdateEnviornmentDisplay(Sunlight, environment.resources(Sunlight)))
    val vbox = new VBox(10) {
      children = Seq(
        Text("Environment"), 
        environmentList, 
        organismsHBox,
        createOrganismButton
      )
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