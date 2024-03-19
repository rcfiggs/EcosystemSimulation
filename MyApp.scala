import scalafx.Includes._
import scala.jdk.CollectionConverters._
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ListView}

case class MyObject(name: String)

object MyApp extends JFXApp3 {
  override def start(): Unit = {
    val listView = new ListView[String]()
    val myObjects = List(MyObject("Item 1"), MyObject("Item 2"), MyObject("Item 3"), MyObject("Item 4"), MyObject("Item 5"))
    listView.getItems().addAll(myObjects.map(_.name).asJava)
    listView.onMouseClicked = (event) => {
      println(s"Item ${listView.getSelectionModel().getSelectedIndex()} clicked!")
    }

    val scene = new Scene(listView, 300, 200)

    stage = new PrimaryStage()
    stage.scene = scene
    stage.show()
  }
}