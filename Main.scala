import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.BorderPane
import scalafx.scene.canvas.Canvas
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color
import scalafx.application.JFXApp3.PrimaryStage

import scalafx.animation.Timeline
import scalafx.event.ActionEvent
import scalafx.util.Duration

import scalafx.Includes._
import scalafx.animation.KeyFrame

case class GameObject(name:String, value: Int = (Math.random() * 10).toInt)
object GameState{
  var objects: List[GameObject] = List[GameObject]()
}

object MyScalaFXProject extends JFXApp3 {
  override def start(): Unit = {
    val canvas = new Canvas(600, 400)
    val gc = canvas.graphicsContext2D
    
    //Draw Green Triangle to canvas
    val xPoints = Array[Double](100, 200, 100)
    val yPoints= Array[Double](100, 200, 200)
    
    GameState.objects = GameState.objects.appendedAll((0 to 20).map((i: Int) => GameObject(i.toString)))
    // Initial text rendering
    renderText(gc, GameState.objects)    
    
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
    
    
    val timeline = new Timeline {
      cycleCount = Timeline.Indefinite
      keyFrames = KeyFrame(Duration(1000), onFinished = (e: ActionEvent) => {
        // Clear the canvas
        gc.clearRect(0, 0, canvas.getWidth, canvas.getHeight)
        
        // Get the updated list of game objects (replace with actual logic for updating/fetching game objects)
        val updatedGameObjects = GameState.objects 
        
        // Redraw the triangle if needed
        gc.setFill(Color.Green)
        gc.fillPolygon(xPoints, yPoints, 3)
        
        // Render the updated game objects
        renderText(gc, updatedGameObjects)
      })
    }
    timeline.play()
    
  }
  def renderText(gc: GraphicsContext, objects: List[GameObject]): Unit = {
    gc.setFill(Color.Black) 
    gc.setStroke(Color.Blue) // Color for box outline
    
    val boxWidth = 100 
    val boxHeight = 30
    val maxItemsPerColumn = 5
    val columnSpacing = 20
    
    objects.zipWithIndex.foreach{ case (obj, index) =>
      val x = 10 + (index % maxItemsPerColumn) * (boxWidth + columnSpacing)
      val y = 20 + (index / maxItemsPerColumn) * boxHeight
      
      // Draw the box
      gc.strokeRect(x, y, boxWidth, boxHeight)
      
      // Draw the text within the box (adjust offsets as needed)
      gc.fillText(obj.toString, x + 5, y + 20) 
    }
  }
  
}