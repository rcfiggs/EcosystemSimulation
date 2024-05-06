package model.resources

import org.scalatest.funsuite.AnyFunSuite

class ResourceConversionsSuite extends AnyFunSuite {

  test("Conversion should succeed with sufficient inputs") {
    val inputs: Map[Resource, Int] = Map(Water -> 2, Sunlight -> 1)
    val outputs: Map[Resource, Int] = Map(Sugar -> 1)
    val conversion = TestConversion(inputs = inputs, outputs = outputs)
    val result = conversion(inputs)
    assert(result === Some(outputs))
  }

  test("Conversion should fail with insufficient inputs") {
    val inputs: Map[Resource, Int] = Map(Water -> 2, Sunlight -> 1)
    val outputs: Map[Resource, Int] = Map(Sugar -> 1)
    val conversion = TestConversion(inputs = inputs, outputs = outputs)
    val result = conversion(Map(Water -> 1, Sunlight -> 1))
    assert(result === None)
  }
}