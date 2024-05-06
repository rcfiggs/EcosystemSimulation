package model.dna

import model.resources._
import model.dna.{DNA, IntakeEntry, ExtractionEntry, SynthesisEntry, CapacityEntry}

import org.scalatest.flatspec.AnyFlatSpec 
import org.scalatest.funsuite.AnyFunSuite

class DNASuite extends AnyFunSuite {
  val dna = DNA(
    intake = Map(Sunlight -> 1),
    extraction = Map(Water -> 2),
    synthesis = Map(ProduceSugar -> 1),
    capacity = Map(Water -> 10, Sunlight -> 10, Sugar -> 10)
)

  test("toEntries should return a list of DNAEntries representing the current state"){
    val entries = dna.toEntries
    val expected = Seq(
      IntakeEntry(Sunlight, 1),
      ExtractionEntry(Water, 2),
      SynthesisEntry(ProduceSugar, 1),
      CapacityEntry(Water, 10), CapacityEntry(Sunlight, 10), CapacityEntry(Sugar, 10)
    )
    assert(entries === expected)
  }

  test("withModifiedProperty should update intake") {
    val newDna = dna.withModifiedProperty(IntakeEntry(Sunlight, 0))
    assert(newDna.intake(Sunlight) === math.ceil(1 * 1.2).toInt)
  }

  test("withModifiedProperty should update extraction") {
    val newDna = dna.withModifiedProperty(ExtractionEntry(Water, 0))
    assert(newDna.extraction(Water) === math.ceil(2 * 1.2).toInt)
  }

  test("withModifiedProperty should update synthesis") {
    val newDna = dna.withModifiedProperty(SynthesisEntry(ProduceSugar, 0))
    assert(newDna.synthesis(ProduceSugar) === math.ceil(1 * 1.2).toInt)
  }

  test("withModifiedProperty should update capacity") {
    val newDna = dna.withModifiedProperty(CapacityEntry(Water, 0))
    assert(newDna.capacity(Water) === math.ceil(10 * 1.2).toInt)
  }
}
