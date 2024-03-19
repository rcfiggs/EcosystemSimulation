import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ListView}
import scalafx.scene.layout.GridPane


object MyApp extends JFXApp3 {
  override def start(): Unit = {
    val grid = new GridPane()
    grid.hgap = 10
    grid.vgap = 10

    val listView = new ListView[String]()
    listView.getItems().addAll("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    listView.onMouseClicked = (event) => {
      println(s"Item ${listView.getSelectionModel().getSelectedIndex()} clicked!")
    }

    grid.add(listView, 0, 0)

    val scene = new Scene(grid, 300, 200)

    stage = new PrimaryStage()
    stage.scene = scene
    stage.show()
  }
}