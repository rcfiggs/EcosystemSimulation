package ecoApp

case class UpdateOrganismDisplay(organism: Organism) extends Event {
  override val targetId = Entities.organismDisplay
}