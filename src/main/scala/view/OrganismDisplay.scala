package view

import model.entities.{Entity, Entities, Organism}
import model.entities.organisms.PerishedOrganism
import model.events.{
  Event, RemoveFromDisplay, UpdateDNADisplay, OrganismSelected, UpdateOrganismDisplay,
  AddOrganismToDisplay, RemoveOrganismFromDisplay,
  EventEmitter, ConditionalEmitter,
}

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ListView
import scala.collection.mutable
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import scalafx.scene.control.ListCell


class OrganismDisplay(val dataList: ObservableBuffer[Organism], listView: ListView[Organism]) extends Entity {
  override val id = Entities.organismDisplay
  val organismMap: mutable.Map[Long, Int] = mutable.Map[Long,Int]()
  
  listView.getSelectionModel.selectedIndexProperty.addListener(new ChangeListener[Number] {
    override def changed(observable: ObservableValue[? <: Number], oldIndex: Number, newIndex: Number): Unit = {
      val selectedOrganism = listView.getSelectionModel.getSelectedItem
      if (selectedOrganism != null) {
        events.enqueue(OrganismSelected(id, selectedOrganism))
      }
    }
  })
  listView.cellFactory = (cell: ListCell[Organism], organism: Organism) => {
    cell.text = organism.display
  }
  
  def eventEmitters: Seq[EventEmitter] = Seq()
  val eventHandlers = {
    case AddOrganismToDisplay(organism) => {
      val index = dataList.indexWhere(_.getClass.getSimpleName > organism.getClass.getSimpleName)
      if (index == -1) {
        dataList.add(organism)
        organismMap(organism.id) = dataList.size - 1
      } else {
        dataList.insert(index, organism)
        organismMap.foreach {
          case (id, idx) => {
            if (idx >= index) {
              organismMap(id) = idx + 1
            }
          }
        }
        organismMap(organism.id) = index
      }
      Seq()
    }
    case RemoveOrganismFromDisplay(id) => {
      if(organismMap.contains(id)){
        val index = organismMap(id)
        dataList.remove(index)
        organismMap -= id
        organismMap.foreach {
          case (id, idx) => {
            if (idx > index) {
              organismMap(id) = idx - 1
            }
          }
        }
      }
      Seq()
    }
    case event: UpdateOrganismDisplay => {
      val organism: Organism = event.organism
      if(organismMap.contains(organism.id)){
        dataList.update(organismMap(organism.id), organism)
      } else {
        organismMap(organism.id) = dataList.size
        dataList.add(organism)
      }
      Seq()
    }
    case OrganismSelected(_, organism) => {
      Seq(
      UpdateDNADisplay(organism.dna),
      OrganismSelected(Entities.createOrganismButton, organism)
      )
    }
    case RemoveFromDisplay(id) => {
      val index = organismMap(id)
      dataList.remove(index)
      organismMap -= id
      organismMap.foreach {
        case (id, idx) => {
          if (idx > index) {
            organismMap(id) = idx - 1
          }
        }
      }
      Seq()
    }
    case _ => Seq()
  }
}