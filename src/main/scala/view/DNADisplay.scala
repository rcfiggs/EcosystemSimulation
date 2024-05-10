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


object DNADisplay extends Entity {
  override val id = Entities.dnaDisplay

  private val dataList = ObservableBuffer[DNAEntry]()
  private val listView = new ListView[DNAEntry](dataList)

  def getView: ListView[DNAEntry] = listView

  def eventEmitters: Seq[EventEmitter] = Seq()

  val eventHandlers: PartialFunction[Event, Seq[Event]] = {
    case event: UpdateDNADisplay =>
      dataList.clear()
      dataList ++= event.dna.toEntries
      Seq()
    case DNAEntrySelected(_, dnaEntry) =>
      Seq(DNAEntrySelected(Entities.createOrganismButton, dnaEntry))
    case _ => Seq()
  }
}