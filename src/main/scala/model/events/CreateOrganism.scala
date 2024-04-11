package ecoApp

case class CreateOrganism(organism: Organism) extends Event{
  override val targetId = Entities.entityManager
}