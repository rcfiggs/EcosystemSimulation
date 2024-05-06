package view

import model.entities.{Entity, Entities, Organism}
import model.events.{
  Event, ButtonPressed, Reproduce, OrganismSelected, DNAEntrySelected,
  EventEmitter,
  eventToSeq,
}
import model.dna.{DNAEntry}

import scalafx.scene.control.{ Button, ChoiceBox}

class CreateOrganismButton(button: Button) extends Entity {
  override val id = Entities.createOrganismButton
  button.onAction = (_) => {
    this.events.enqueue(ButtonPressed(id))
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
