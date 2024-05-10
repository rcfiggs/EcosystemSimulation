package view

import model.entities.Entity
import model.entities.Entities
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.control.Button
import model.events.{OrganismSelected, Reproduce, Forward, eventToSeq}
import model.entities.organisms.Plant
import model.entities.organisms.Animal
import model.entities.organisms.Fungi
import scalafx.stage.Modality
import model.events.EventEmitter
import model.events.Event
import model.entities.Organism
import model.dna.DNAEntry
import scalafx.scene.control.ListView
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.HBox

object OrganismSelectionWindow extends Entity {
  override val id = Entities.createOrganismWindow

  private val stage = new Stage {
    title = "Select DNA Property"
    initModality(Modality.ApplicationModal)
  }

  private var selectedOrganism: Option[Organism] = None
  private var selectedDNAEntry: Option[DNAEntry] = None

  def eventEmitters: Seq[EventEmitter] = Seq()

  val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case OrganismSelected(_, organism) => {
      selectedOrganism = Some(organism)
      Seq()
    }
    case _ => Seq()
  }

  def showAndWait(): Unit = {
    val dnaEntries = selectedOrganism match {
      case Some(organism) => organism.dna.toEntries
      case None => Seq() // Handle the case where no organism is selected
    }

    val listView = new ListView[DNAEntry](dnaEntries) {
      this.getSelectionModel.selectedItemProperty.addListener { (_, _, newValue) =>
        selectedDNAEntry = Some(newValue)
      }
    }

    val confirmButton = new Button("Confirm") {
      disable = true
      onAction = (_) => {
        selectedOrganism match {
          case Some(organism) => selectedDNAEntry match {
            case Some(dnaEntry) => events.enqueue(Forward(Reproduce(organism.id, dnaEntry)))
            case None => // This should not happen
          }
          case None => // This should not happen
        }
        stage.close()
      }
    }

    val cancelButton = new Button("Cancel") {
      onAction = (_) => {
        stage.close()
      }
    }

    listView.getSelectionModel.selectedItemProperty.addListener { (_, _, _) =>
      confirmButton.disable = listView.getSelectionModel.getSelectedItem == null
    }

    stage.scene = new Scene {
      root = new BorderPane {
        center = listView
        bottom = new HBox {
          children = Seq(confirmButton, cancelButton)
        }
      }
    }
    stage.showAndWait()
  }
}