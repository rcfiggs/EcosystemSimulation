package model.dna

import model.resources._
import model.dna.{DNA, DNAEntry, DNAMutation, Extraction, Consumption, Synthesis, Capacity, InitialResource}

import org.scalatest.flatspec.AnyFlatSpec 
import org.scalatest.funsuite.AnyFunSuite

class DNASuite extends AnyFunSuite {
  val dna = DNA(
    properties = Map(
      Extraction(Sunlight) -> 1,
      Consumption(Water) -> 2,
      Synthesis(ProduceSugar) -> 1,
      Capacity(Water) -> 10,
      Capacity(Sunlight) -> 10,
      Capacity(Sugar) -> 10,
      InitialResource(Water) -> 0,
      InitialResource(Sunlight) -> 0,
      InitialResource(Sugar) -> 0,
    )
  )

  test("toEntries should return a list of DNAEntries representing the current state"){
    val entries = dna.toEntries
    val expected = Seq(
      DNAEntry(Extraction(Sunlight), 1),
      DNAEntry(Consumption(Water), 2),
      DNAEntry(Synthesis(ProduceSugar), 1),
      DNAEntry(Capacity(Water), 10),
      DNAEntry(Capacity(Sunlight), 10),
      DNAEntry(Capacity(Sugar), 10),
      DNAEntry(InitialResource(Water), 0),
      DNAEntry(InitialResource(Sunlight), 0),
      DNAEntry(InitialResource(Sugar), 0),
    )
    assert(entries.forall(dnaEntry => expected.contains(dnaEntry)))
  }

  test("withMutation should update property") {
    val mutation = DNAMutation(Extraction(Sunlight), 0.5)
    val newDna = dna.withMutation(mutation)
    assert(newDna.properties(Extraction(Sunlight)) === (1 * (1 + 0.5)).toInt)
  }

  test("getRandomMutations should return a sequence of random mutations") {
    val mutations = dna.randomMutations
    assert(mutations.size === 1)
    assert(mutations.forall((mutation) => mutation.modifier >= -1 && mutation.modifier <= 1))
  }
}