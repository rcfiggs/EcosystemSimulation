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
import scalafx.scene.shape.Rectangle
import scalafx.scene.control.ContextMenu
import scalafx.scene.control.MenuItem

case class GameObject(
  name: String, 
  value: Int = (Math.random() * 10).toInt,
  
){
  def getActions(): List[String] = List("Print", "Name")
   def performAction(action: String) = {
    action match 
      case "Print" => println("printed")
      case "Name" => println(name)
  }
}

object GameState {
  var objects: List[GameObject] = List[GameObject]()
}

class GameCanvas(width: Double, height: Double) extends Canvas(width, height) {
  val gc = graphicsContext2D
  
  def drawGreenTriangle(): Unit = {
    val xPoints = Array[Double](100, 200, 100)
    val yPoints = Array[Double](100, 200, 200)
    gc.setFill(Color.Green)
    gc.fillPolygon(xPoints, yPoints, 3)
  }
  
  def renderText(objects: List[GameObject]): Unit = {
    gc.setFill(Color.Black)
    gc.setStroke(Color.Blue) // Color for box outline
    
    val boxWidth = 100
    val boxHeight = 30
    val maxItemsPerColumn = 5
    val columnSpacing = 20
    
    objects.zipWithIndex.foreach { case (obj, index) =>
      val column = index % maxItemsPerColumn
      val row = index / maxItemsPerColumn
      val x = 10 + (column * (boxWidth + 20))
      val y = 20 + (row * boxHeight)
      
      // Draw the box
      gc.strokeRect(x, y, boxWidth, boxHeight)
      
      // Draw the text within the box (adjust offsets as needed)
      gc.fillText(obj.toString, x + 5, y + 20)
      
      // Add a mouse click event handler to the box
      val box = Rectangle(x, y, boxWidth, boxHeight)
      box.setOnMouseClicked((event) => {
        // Display the list of available actions for the GameObject
        val actions = obj.getActions()
        val actionMenu = new ContextMenu()
        actions.foreach((action) => {
          val menuItem = new MenuItem(action)
          menuItem.setOnAction((event) => {
            // Perform the action on the GameObject
            obj.performAction(action)
          })
          actionMenu.getItems.add(menuItem)
        })
        actionMenu.show(this, event.getScreenX, event.getScreenY)
      })
    }
  }
}

object GameUI extends JFXApp3 {
  override def start(): Unit = {
    val gameCanvas = new GameCanvas(600, 400)
    GameState.objects = GameState.objects.appendedAll((0 to 20).map((i: Int) => GameObject(i.toString)))
    gameCanvas.drawGreenTriangle()
    gameCanvas.renderText(GameState.objects)
    
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
          top = gameCanvas
          bottom = buttons
        }
      }
    }
    
    val timeline = new Timeline {
      cycleCount = Timeline.Indefinite
      keyFrames = KeyFrame(Duration(1000), onFinished = (e: ActionEvent) => {
        // Clear the canvas
        gameCanvas.gc.clearRect(0, 0, gameCanvas.getWidth, gameCanvas.getHeight)
        
        // Get the updated list of game objects (replace with actual logic for updating/fetching game objects)
        val updatedGameObjects = GameState.objects
        
        // Redraw the triangle if needed
        gameCanvas.drawGreenTriangle()
        
        // Render the updated game objects
        gameCanvas.renderText(updatedGameObjects)
      })
    }
    timeline.play()
  }
}