import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.Button


object MyApp extends JFXApp3 {
  override def start(): Unit = {
    val button = new Button("Click me!")
    button.onAction = (event) => {
      println("Button clicked!")
    }

    val scene = new Scene(button, 300, 200)

    stage = PrimaryStage()
    stage.scene = scene
    stage.show()
  }
}