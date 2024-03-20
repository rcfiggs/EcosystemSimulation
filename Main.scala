import scalafx.Includes._
import scala.jdk.CollectionConverters._
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ListView}
import scalafx.event.ActionEvent
import scala.compiletime.uninitialized

case class MyObject(name: String, var value: Int) {
  def incrementValue(): Unit = {
    value += 1
  }
}

// GameState.scala
class GameState {
  private var myObjects: List[MyObject] = List(MyObject("Item 1", 1), MyObject("Item 2", 2), MyObject("Item 3", 3), MyObject("Item 4", 4), MyObject("Item 5", 5))

  def incrementValues(): Unit = {
    println("inc")
    println(myObjects)
    myObjects.foreach(_.incrementValue())
  }

  def getObjectValues(): List[String] = {
    myObjects.map(obj => s"${obj.name}: ${obj.value}")
  }
}

// GameView.scala
class GameView(game: Game) {
  val listView = new ListView[String]()
  val nextDayButton = new Button("Next Day")

  nextDayButton.onAction = (_: ActionEvent) => {
    game.handleNextDay()
  }

  def start(): Scene = {
    val scene = new Scene(new javafx.scene.layout.VBox(listView, nextDayButton), 300, 200)
    update()
    scene
  }

  def update(): Unit = {
    listView.getItems().clear()
    listView.getItems().addAll(game.getObjectValues().asJava)
  }
}

// GameController.scala
class GameController(game: Game, view: GameView) {
  def handleNextDay(): Unit = {
    game.incrementValues()
    view.update()
  }
}

// Game.scala
class Game {
  private val state: GameState = new GameState()

  def incrementValues(): Unit = {
    state.incrementValues()
  }

  def getObjectValues(): List[String] = {
    state.getObjectValues()
  }

  def handleNextDay(): Unit = {
    incrementValues()
  }
}

// Main.scala
object Main extends JFXApp3 {
  val game = new Game()
  override def start(): Unit = {
    val view = new GameView(game)
    val controller = new GameController(game, view)
    stage = new PrimaryStage()
    stage.scene = view.start()
    stage.show()
  }
}