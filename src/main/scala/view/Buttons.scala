package view

import model.entities.{Entity, Entities, Organism}
import model.events.{
  Event, ButtonPressed, Reproduce, OrganismSelected, DNAEntrySelected,
  EventEmitter, Pause, Play,
  eventToSeq,
}
import model.dna.{DNAEntry}

import scalafx.scene.control.{ Button, ChoiceBox}
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import model.entities.organisms.Plant
import model.entities.organisms.Animal
import model.entities.organisms.Fungi
import scalafx.stage.Modality

class CreateOrganismButton(button: Button) extends Entity {
  override val id = Entities.createOrganismButton
  button.onAction = (_) => {
    OrganismSelectionWindow.showAndWait()
  }

  private var selectedOrganism: Option[Organism] = None
  private var selectedDNAEntry: Option[DNAEntry] = None

  def eventEmitters: Seq[EventEmitter] = Seq()
  
  val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: ButtonPressed => {
      (selectedOrganism, selectedDNAEntry) match {
        case (Some(organism), Some(dnaEntry)) => Reproduce(organism.id, dnaEntry)
        case _ => Seq() 
      }
    }
    case OrganismSelected(_, organism) => {
      selectedOrganism = Some(organism)
      Seq()
    }
    case DNAEntrySelected(_, dnaEntry) => {
      selectedDNAEntry = Some(dnaEntry)
      Seq()
    }
    case _ => Seq[Event]()
  }
}

class PauseButton(button: Button) extends Entity {
  override val id = Entities.pauseButton
  button.onAction = (_) => {
    this.events.enqueue(ButtonPressed(id))
  }

  def eventEmitters: Seq[EventEmitter] = Seq()

  val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: ButtonPressed => Seq(Pause)
    case _ => Seq[Event]()
  }
}

class PlayButton(button: Button) extends Entity {
  override val id = Entities.playButton
  button.onAction = (_) => {
    this.events.enqueue(ButtonPressed(id))
  }

  def eventEmitters: Seq[EventEmitter] = Seq()

  val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: ButtonPressed => Seq(Play)
    case _ => Seq[Event]()
  }
}