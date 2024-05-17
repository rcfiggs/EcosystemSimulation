package view

import model.entities.{Entity, Entities, Organism}
import model.events.{
  Event, ButtonPressed, Reproduce, OrganismSelected, DNAEntrySelected,
  EventEmitter, Pause, Play, Forward,
  eventToSeq,
}
import model.dna.{DNAEntry}

import scalafx.scene.control.{Button, ChoiceBox}
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import model.entities.organisms.Plant
import model.entities.organisms.Animal
import model.entities.organisms.Fungi
import scalafx.stage.Modality

object CreateOrganismButton extends Entity {
  override val id = Entities.createOrganismButton

  private val button = new Button("Create Organism") {
    onAction = (_) => {
      OrganismSelectionWindow.showAndWait()
    }
  }
  def getButton: Button = button

  def eventEmitters: Seq[EventEmitter] = Seq()

  val eventHandlers: PartialFunction[Event, Seq[Event]] = PartialFunction.empty
}

object PauseButton extends Entity {
  override val id = Entities.pauseButton

  private val button = new Button("Pause") {
    onAction = (_) => {
      events.enqueue(Forward(Pause))
    }
  }
  def getButton: Button = button

  def eventEmitters: Seq[EventEmitter] = Seq()

  val eventHandlers: PartialFunction[Event, Seq[Event]] = PartialFunction.empty
}

object PlayButton extends Entity {
  override val id = Entities.playButton

  private val button = new Button("Play") {
    onAction = (_) => {
      events.enqueue(Forward(Play))
    }
  }
  def getButton: Button = button

  def eventEmitters: Seq[EventEmitter] = Seq()

  val eventHandlers: PartialFunction[Event, Seq[Event]] = PartialFunction.empty
}