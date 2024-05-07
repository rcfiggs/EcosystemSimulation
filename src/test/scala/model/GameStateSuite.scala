package model

import org.scalatest.funsuite.AnyFunSuite
import model.entities.TestEntity
import model.events._

class GameStateSuite extends AnyFunSuite {
  
  test("Add entity to game state") {
    val gameState = new GameState
    val entity = TestEntity(id = 1)
    gameState.addEntity(entity)
    assert(gameState.entities.size === 1)
    assert(gameState.entities.get(1) === Some(entity))
  }
  
  test("Get entity by id") {
    val gameState = new GameState
    val entity = TestEntity(id = 1)
    gameState.addEntity(entity)
    val retrievedEntity = gameState.getEntity(1)
    assert(retrievedEntity === entity)
  }
  
  test("Get non-existent entity by id") {
    val gameState = new GameState
    assertThrows[NoSuchElementException] {
      gameState.getEntity(1)
    }
  }
  
  test("Update entity in game state") {
    val gameState = new GameState
    val entity = TestEntity(id = 1)
    gameState.addEntity(entity)
    val updatedEntity = TestEntity(id = 1, eventEmitters = Seq()) // update with a new emitter
    gameState.setEntity(updatedEntity)
    assert(gameState.entities.get(1) === Some(updatedEntity))
  }
  
  test("Remove entity from game state") {
    val gameState = new GameState
    val entity = TestEntity(id = 1)
    gameState.addEntity(entity)
    gameState.removeEntity(1)
    gameState.processFrame(0)
    assert(gameState.entities.isEmpty)
  }
  
  test("Process frame with update events") {
    val gameState = new GameState
    val entity = TestEntity(id = 1, eventEmitters = Seq(TestEmitter(Seq(TestEvent()))))
    gameState.addEntity(entity)
    
    // Add an event to the entity's events queue
    entity.events.enqueue(TestEvent())
    
    gameState.processFrame(0)
    
    // Verify that the event was processed correctly
    assert(entity.events.isEmpty)
  }
  
  test("Process frame with process events") {
    val gameState = new GameState
    val entity = TestEntity(id = 1, eventEmitters = Seq(TestEmitter(Seq(TestEvent(targetId = 1)))))
    gameState.addEntity(entity)
    
    gameState.processFrame(0)
    
    // Verify that the event was processed correctly
    assert(entity.events.size === 1)
    assert(entity.events.head === TestEvent(targetId = 1))
  }
  
  test("Pause and unpause game state") {
    val gameState = new GameState
    val entity = TestEntity(id = 1, eventEmitters = Seq(TestEmitter(Seq(TestEvent(targetId = 1)))))
    gameState.addEntity(entity)
    
    // Pause the game state
    gameState.paused = true
    
    // Process a frame while paused
    gameState.processFrame(1000)
    
    // Verify that no events were processed
    assert(entity.events.isEmpty)
    
    // Unpause the game state
    gameState.paused = false
    
    // Process another frame
    gameState.processFrame(2000)
    
    // Verify that an event was processed
    assert(entity.events.size === 1)
    assert(entity.events.head === TestEvent(targetId = 1))
  }
  
  test("Game time increments correctly when not paused") {
    val gameState = new GameState
    gameState.processFrame(1000)
    assert(gameState.gameTime === 1000)
    gameState.processFrame(2000)
    assert(gameState.gameTime === 2000)
  }
  
  test("Game time does not increment when paused") {
    val gameState = new GameState
    gameState.paused = true
    gameState.processFrame(1000)
    assert(gameState.gameTime === 0)
    gameState.processFrame(2000)
    assert(gameState.gameTime === 0)
  }
}