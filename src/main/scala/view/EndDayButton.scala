package ecoApp

import scalafx.scene.control.Button
import model.Resources._

class EndDayButton(gameState: GameState, button: Button) extends Entity {
  override val id = Entities.endDayButton
  button.onAction = (_) => {
    this.events.enqueue(ButtonPressed(id))
  }

  def eventEmitters = Seq()
  
  val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: ButtonPressed => {
      Seq(SpendResources(
        targetId = 7L,
        resources = Map(Starch -> 1),
        resultingEvents = Seq(ResourceGain(7L, Cellulose, 1)),
      ))
    }
    case _ => Seq[Event]()
  }
}