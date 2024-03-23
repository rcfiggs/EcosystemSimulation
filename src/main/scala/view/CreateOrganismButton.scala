package ecoApp

import scalafx.scene.control.{ Button, ChoiceBox}

class CreateOrganismButton(button: Button, choiceBox: ChoiceBox[String]) extends Entity {
  override val id = Entities.createOrganismButton
  button.onAction = (_) => {
    this.events.enqueue(ButtonPressed(id))
  }
  
  override val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: ButtonPressed => {
      val organismType = choiceBox.getValue
      organismType match {
        case "Plant" => Seq(CreateOrganism(new Plant(birthday = 0)))
        case "Animal" => Seq(CreateOrganism(new Animal(birthday = 0)))
        case "Fungi" => Seq(CreateOrganism(new Fungi(birthday = 0)))
        case _ => Seq[Event]()
      }
    }
    case _ => Seq[Event]()
  }
  
  override def process(time: Long): Seq[Event] = {
    Seq[Event]()
  }
}
