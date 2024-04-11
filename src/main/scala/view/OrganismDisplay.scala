package ecoApp

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ListView
import scala.collection.mutable
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import scalafx.scene.control.ListCell

case class UpdateOrganismDisplay(organism: Organism) extends Event {
  override val targetId = Entities.organismDisplay
}

class OrganismDisplay(dataList: ObservableBuffer[Organism], listView: ListView[Organism]) extends Entity {
  override val id = Entities.organismDisplay
  val organismMap: mutable.Map[Long, Int] = mutable.Map[Long,Int]()
  listView.getSelectionModel.selectedIndexProperty.addListener(new ChangeListener[Number] {
    override def changed(observable: ObservableValue[? <: Number], oldIndex: Number, newIndex: Number): Unit = {
      println(s"Selected: ${listView.getSelectionModel.getSelectedItem.display}")
    }
  })
  listView.cellFactory = (cell: ListCell[Organism], organism: Organism) => cell.text = organism.display
  
  def eventEmitters: Seq[EventEmitter] = Seq()
  val eventHandlers = {
    case event: UpdateOrganismDisplay =>
    val organism: Organism = event.organism
    if(organismMap.contains(organism.id)){
      dataList.update(organismMap(organism.id), organism) // replace name with data to be displayed
    } else {
      organismMap(organism.id) = dataList.size
      dataList.add(organism)
    }
    Seq[Event]() // return an empty sequence of events
    case _ => Seq[Event]() // handle other event types
  }
}