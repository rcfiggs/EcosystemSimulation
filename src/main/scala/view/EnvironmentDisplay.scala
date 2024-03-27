package view

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ListView
import scala.collection.mutable
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import scalafx.scene.control.ListCell
import ecoApp._

case class UpdateEnviornmentDisplay(propertyName: String, value: String) extends Event {
  override val targetId = Entities.environmentDisplay
}

class EnvironmentDisplay(dataList: ObservableBuffer[String], listView: ListView[String]) extends Entity {
  override val id = Entities.environmentDisplay
  val propertyMap = mutable.Map[String, Int]()
  
  def eventEmitters: Seq[EventEmitter] = Seq()
  val eventHandlers = {
    case event: UpdateEnviornmentDisplay => {
      if(propertyMap.contains(event.propertyName)){
        dataList.update(propertyMap(event.propertyName), s"${event.propertyName}: ${event.value}") // replace name with data to be displayed
      } else {
        propertyMap(event.propertyName) = dataList.size
        dataList.add(s"${event.propertyName}: ${event.value}")
      }
      Seq()
    }
  }
}