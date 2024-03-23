package ecoApp

case class EndDay(organism: Organism) extends Event {
  override val targetId =  organism.id
}