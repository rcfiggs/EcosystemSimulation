package view

import model.entities.Entity
import model.entities.Entities
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.control.Button
import model.events.OrganismSelected
import model.entities.organisms.Plant
import model.entities.organisms.Animal
import model.entities.organisms.Fungi
import scalafx.stage.Modality
import model.events.EventEmitter
import model.events.Event

object OrganismSelectionWindow extends Entity {
  override val id = Entities.createOrganismWindow

  private val stage = new Stage {
    title = "Select Organism"
    scene = new Scene {
      root = new VBox {
        children = Seq(
          new Button("Plant") {
            onAction = (_) => {
              events.enqueue(OrganismSelected(Entities.createOrganismWindow, new Plant()))
              close()
            }
          },
          new Button("Animal") {
            onAction = (_) => {
              events.enqueue(OrganismSelected(Entities.createOrganismWindow, new Animal()))
              close()
            }
          },
          new Button("Fungi") {
            onAction = (_) => {
              events.enqueue(OrganismSelected(Entities.createOrganismWindow, new Fungi()))
              close()
            }
          }
        )
      }
    }
    initModality(Modality.ApplicationModal)
  }

  def eventEmitters: Seq[EventEmitter] = Seq()

  val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case _ => Seq()
  }

  def showAndWait(): Unit = {
    stage.showAndWait()
  }
}
