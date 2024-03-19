import scalafx.Includes._
import scala.jdk.CollectionConverters._
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ListView}

case class MyObject(name: String, var value: Int) {
  def incrementValue(): Unit = {
    value += 1
  }
}

object MyApp extends JFXApp3 {
  override def start(): Unit = {
    val listView = new ListView[String]()
    val myObjects = List(MyObject("Item 1", 1), MyObject("Item 2", 2), MyObject("Item 3", 3), MyObject("Item 4", 4), MyObject("Item 5", 5))
    listView.getItems().addAll(myObjects.map(obj => s"${obj.name}: ${obj.value}").asJava)

    listView.onMouseClicked = (event) => {
      val selectedIndex = listView.getSelectionModel().getSelectedIndex()
      if (selectedIndex != -1) {
        myObjects(selectedIndex).incrementValue()
        listView.getItems().set(selectedIndex, s"${myObjects(selectedIndex).name}: ${myObjects(selectedIndex).value}")
      }
    }

    val scene = new Scene(listView, 300, 200)

    stage = new PrimaryStage()
    stage.scene = scene
    stage.show()
  }
}