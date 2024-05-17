
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
import view.{DNADisplay, OrganismDisplay, CreateOrganismButton, PauseButton, PlayButton}
import model.GameState
import model.entities.{Entity, Entities, GameStateManager, Organism}
import model.entities.organisms.{Plant, Animal, Fungi}
import model.dna.{DNAEntry}
import model.events.{Event, UpdateOrganismDisplay, UpdateEnviornmentDisplay}
import model.resources.{Water, Nutrient, Sunlight}
import view.OrganismSelectionWindow


object SimpleApp extends JFXApp3 {
  val gameState: GameState = new GameState()
  
  override def start(): Unit = {

    gameState.addEntity(OrganismDisplay)
    gameState.addEntity(DNADisplay)

    val environment = Environment
    gameState.addEntity(environment)

    val organismsHBox = new HBox(10) {
      children = Seq(
        new VBox(10) {
          children = Seq(
            Text("Organisms"),
            OrganismDisplay.getView
          )
          hgrow = Priority.Always
        },
        new VBox(10) {
          children = Seq(
            Text("Organism DNA"),
            DNADisplay.getView
          )
          hgrow = Priority.Always
        }
      )
    }
    gameState.addEntity(OrganismSelectionWindow)
    
    val firstAnimal = new Animal()
    gameState.addEntity(firstAnimal)
    gameState.getEntity(Entities.organismDisplay).events.enqueue(UpdateOrganismDisplay(firstAnimal))
    
    val firstFungi = new Fungi()
    gameState.addEntity(firstFungi)
    gameState.getEntity(Entities.organismDisplay).events.enqueue(UpdateOrganismDisplay(firstFungi))
    
    val firstPlant = new Plant()
    gameState.addEntity(firstPlant)
    gameState.getEntity(Entities.organismDisplay).events.enqueue(UpdateOrganismDisplay(firstPlant))
    
    gameState.addEntity(CreateOrganismButton)
    gameState.addEntity(PauseButton)
    gameState.addEntity(PlayButton)
    
    val gameStateManager = GameStateManager(gameState)
    gameState.addEntity(gameStateManager)

    gameState.addEntity(EnvironmentDisplay)
    gameState.getEntity(Entities.environmentDisplay).events.enqueue(UpdateEnviornmentDisplay(Water, environment.resources(Water)))
    gameState.getEntity(Entities.environmentDisplay).events.enqueue(UpdateEnviornmentDisplay(Nutrient, environment.resources(Nutrient)))
    gameState.getEntity(Entities.environmentDisplay).events.enqueue(UpdateEnviornmentDisplay(Sunlight, environment.resources(Sunlight)))
    val vbox = new VBox(10) {
      children = Seq(
        Text("Environment"), 
        EnvironmentDisplay.getView, 
        organismsHBox,
        CreateOrganismButton.getButton,
        PauseButton.getButton,
        PlayButton.getButton,
      )
    }
    
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(vbox, 800, 600)
    }
    
    AnimationTimer((now: Long) => {
      val time = System.currentTimeMillis()
      gameStateManager.processFrame(time)
    }).start()
  }
}