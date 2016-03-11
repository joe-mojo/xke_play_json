package models

import org.scalatest.{FunSpec, Matchers}

class MappingSpec extends FunSpec with Matchers {

  describe("a class") {

    it("should do something") {
      "hello"
        .toCharArray
        .map(_.toString)
        .map(_.capitalize)
        .mkString shouldBe "HELLO"
    }

  }

}
