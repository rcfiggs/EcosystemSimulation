package ecoApp

case class CreateOrganism(newOrganism: () => Organism) extends Event{
  override val targetId = Entities.entityManager
}