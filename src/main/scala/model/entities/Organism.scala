package ecoApp

trait Organism extends Entity {
  val id: Long = Entities.newId
  val birthday: Int
  var energy: Int = 100
  var hydration: Int = 100
  var lastUpdateTime: Long = -1 // new field to track the last update time
  
  def process(time: Long): Seq[Event] = {
    if(lastUpdateTime == -1){
      lastUpdateTime = time
      Seq()
    } else {
      val timeDifference = time - lastUpdateTime // calculate time difference
      this.hydration -= hydrationLossRate(timeDifference) // use time difference to calculate hydration loss
      this.lastUpdateTime = time // update last update time
      Seq(UpdateOrganismDisplay(this))
    }
    
  }
  
  def hydrationLossRate(timeDifference: Long): Int
  
  def display: String = s"${this.getClass.getSimpleName}: Energy: $energy, Hydration: $hydration"
}