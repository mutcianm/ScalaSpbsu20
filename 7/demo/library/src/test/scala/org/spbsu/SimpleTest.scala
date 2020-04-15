package org.spbsu

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class SimpleTest extends AnyFunSuite with Matchers {
  test("simple test") {
    2 + 2 shouldBe 4
  }
}
