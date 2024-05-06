package view

import model.entities.{Entity, Entities}
import model.events.{
  UpdateEnviornmentDisplay,
  EventEmitter,
}
import model.resources.{NaturalResource}

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ListView
import scala.collection.mutable
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import scalafx.scene.control.ListCell




class EnvironmentDisplay(dataList: ObservableBuffer[String], listView: ListView[String]) extends Entity {
  override val id = Entities.environmentDisplay
  val propertyMap = mutable.Map[NaturalResource, Int]()
  
  def eventEmitters: Seq[EventEmitter] = Seq()
  val eventHandlers = {
    case event: UpdateEnviornmentDisplay => {
      if(propertyMap.contains(event.resource)){
        dataList.update(propertyMap(event.resource), s"${event.resource}: ${event.value}") // replace name with data to be displayed
      } else {
        propertyMap(event.resource) = dataList.size
        dataList.add(s"${event.resource}: ${event.value}")
      }
      Seq()
    }
  }
}