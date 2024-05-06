package view


import model.dna.{DNAEntry}
import model.entities.{Entities, Entity}
import model.events.{
  Event, DNAEntrySelected, UpdateDNADisplay, 
  EventEmitter,
  eventToSeq,
}

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ListView
import scala.collection.mutable
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import scalafx.scene.control.ListCell


class DNADisplay(dataList: ObservableBuffer[DNAEntry], listView: ListView[DNAEntry]) extends Entity {
  override val id = Entities.dnaDisplay
  
  listView.cellFactory = (cell: ListCell[DNAEntry], dna: DNAEntry) => cell.text = dna.toString
  
  listView.getSelectionModel.selectedIndexProperty.addListener(new ChangeListener[Number] {
    override def changed(observable: ObservableValue[? <: Number], oldIndex: Number, newIndex: Number): Unit = {
      val selected = listView.getSelectionModel.getSelectedItem
      if (selected != null) {
        events.enqueue(DNAEntrySelected(id, selected))
      }
    }
  })
  
  def eventEmitters: Seq[EventEmitter] = Seq()
  val eventHandlers = {
    case event: UpdateDNADisplay =>
    dataList.clear()
    dataList ++= event.dna.toEntries
    Seq()
    case DNAEntrySelected(_, dnaEntry) =>
      DNAEntrySelected(Entities.createOrganismButton, dnaEntry)
    case _ => Seq()
  }
}

