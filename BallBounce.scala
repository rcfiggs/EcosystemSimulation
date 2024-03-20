import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color
import scalafx.scene.shape.Arc
import scalafx.scene.layout.VBox
import scalafx.animation.AnimationTimer

object BallBounceApp extends JFXApp3 {
  var x = 100.0
  var y = 100.0
  var dx = 5
  var dy = 5
  
  val ballRadius = 20.0
  
  val canvas = new Canvas(800, 600)
  val gc = canvas.graphicsContext2D
  
  override def start(): Unit = {
    val root = new VBox(10) {
      children = Seq(canvas)
      padding = Insets(10)
    }
    
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(root, 800, 600)
    }
    
    AnimationTimer((now: Long) => {
      // Clear the canvas
      gc.clearRect(0, 0, canvas.width.value, canvas.height.value)
      
      // Check for collision with the edge of the canvas
      if (x < ballRadius || x > canvas.width.value - ballRadius) {
        dx = -dx
      }
      if (y < ballRadius || y > canvas.height.value - ballRadius) {
        dy = -dy
      }
      
      // Move the ball
      x += dx
      y += dy
      
      // Draw the ball
      gc.fill = Color.Red
      gc.fillOval(x, y, ballRadius * 2, ballRadius * 2)
    }).start()
  }
}