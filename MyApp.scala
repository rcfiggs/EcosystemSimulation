import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.GridPane


object MyApp extends JFXApp3 {
  override def start(): Unit = {
    val grid = new GridPane()
    grid.hgap = 10
    grid.vgap = 10

    for (i <- 0 to 4) {
      for (j <- 0 to 4) {
        val button = new Button(s"Button $i,$j")
        button.onAction = (event) => {
          println(s"Button $i,$j clicked!")
        }
        grid.add(button, i, j)
      }
    }

    val scene = new Scene(grid, 300, 200)

    stage = new PrimaryStage()
    stage.scene = scene
    stage.show()
  }
}