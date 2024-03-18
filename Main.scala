import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.BorderPane
import scalafx.scene.canvas.Canvas
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color
import scalafx.application.JFXApp3.PrimaryStage

object MyScalaFXProject extends JFXApp3 {
  override def start(): Unit = {
    val canvas = new Canvas(600, 400)
    val gc = canvas.graphicsContext2D

    //Draw Green Triangle to canvas
    gc.setFill(Color.GREEN)
    val xPoints = Array[Double](100, 200, 100)
    val yPoints= Array[Double](100, 200, 200)
    gc.fillPolygon(xPoints, yPoints, 3)

    val buttons = new BorderPane {
      styleClass += "button-panel"
      prefHeight = 100
      children = List(
        new Button("Button 1"),
        new Button("Button 2"),
        new Button("Button 3")
      )
    }
    stage = new PrimaryStage {
      title = "My ScalaFX Project"
      scene = new Scene {
        root = new BorderPane {
          top = canvas
          bottom = buttons
        }
      }
    }
  }
}