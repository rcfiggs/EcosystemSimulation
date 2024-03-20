import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.ListView
import scalafx.scene.layout.VBox
import scalafx.animation.AnimationTimer
import scala.util.Random

object ListViewUpdateApp extends JFXApp3 {
  // val gameState: GameState = new GameState()
  override def start(): Unit = {
    val dataList = ObservableBuffer[String]()
    val listView = new ListView[String](dataList)
    var list: List[Int] = List(1,2,3,4,5)
    dataList.addAll(list.map(_.toString))
    
    val vbox = new VBox(10) {
      children = Seq(listView)
    }
    
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(vbox, 800, 600)
    }
    
    AnimationTimer((now: Long) => {
      // Update the data list
      val updatedList = list.updated(0, Random.nextInt)
      val diff = updatedList.zip(list).collect { case (newItem, oldItem) if newItem != oldItem => (newItem, oldItem) }
      diff.foreach { case (newItem, oldItem) =>
        dataList.update(dataList.indexOf(oldItem.toString), newItem.toString)
      }
      list = updatedList
    }).start()
  }
}