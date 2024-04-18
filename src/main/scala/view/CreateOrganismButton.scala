package ecoApp

import scalafx.scene.control.{ Button, ChoiceBox}

class CreateOrganismButton(button: Button, choiceBox: ChoiceBox[String]) extends Entity {
  override val id = Entities.createOrganismButton
  button.onAction = (_) => {
    this.events.enqueue(ButtonPressed(id))
  }

  def eventEmitters: Seq[EventEmitter] = Seq()
  
  val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: ButtonPressed => {
      val organismType = choiceBox.getValue
      organismType match {
        case "Plant" => Seq(CreateOrganism(() => Plant(0)))
        case "Animal" => Seq(CreateOrganism(() => Animal(0)))
        case "Fungi" => Seq(CreateOrganism(() => Fungi(0)))
        case _ => Seq[Event]()
      }
    }
    case _ => Seq[Event]()
  }
}
