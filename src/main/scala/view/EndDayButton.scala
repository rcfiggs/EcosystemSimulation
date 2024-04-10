package ecoApp

import scalafx.scene.control.Button

class EndDayButton(gameState: GameState, button: Button) extends Entity {
  override val id = Entities.endDayButton
  button.onAction = (_) => {
    this.events.enqueue(ButtonPressed(id))
  }

  def eventEmitters = Seq()
  
  val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: ButtonPressed => {
      gameState.entities.values.collect {
        case organism: Organism[?] => EndDay(organism)
      }.toSeq
    }
    case _ => Seq[Event]()
  }
}