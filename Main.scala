import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color
import scalafx.scene.shape.Arc
import javafx.animation.AnimationTimer
import scalafx.scene.layout.VBox

class BallBounceApp extends JFXApp3 {
  var x = 0.0
  var y = 0.0
  var dx = 0.1
  var dy = 0.1

  val ballRadius = 20.0

  val canvas = new Canvas(800, 600)
  val gc = canvas.graphicsContext2D

  stage = new JFXApp3.PrimaryStage {
    scene = Scene(VBox(canvas))
  }

  new AnimationTimer {
    override def handle(now: Long): Unit = {
      // Clear the canvas
      gc.clearRect(0, 0, canvas.width.value, canvas.height.value)

      // Draw the ball
      gc.fill = Color.Red
      gc.fillOval(x, y, ballRadius * 2, ballRadius * 2)

      // Move the ball
      x += dx
      y += dy

      // Check for collision with the edge of the canvas
      if (x < ballRadius || x > canvas.width.value - ballRadius) {
        dx = -dx
      }
      if (y < ballRadius || y > canvas.height.value - ballRadius) {
        dy = -dy
      }
    }
  }
}