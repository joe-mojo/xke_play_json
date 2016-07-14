package models

import org.scalatest.{Inside, _}
import play.api.libs.json.{JsSuccess, Json}

class CustomerSpec extends FunSpec with ShouldMatchers with Inside {
  describe("A Customer") {
    it("should parse a JSON with Address attributes at top-level and no state"){
      val inputJson =
        """{
          |  "firstName": "Laurent",
          |  "lastName": "Outang",
          |  "number": "3 Bis",
          |  "street": "rue Pin",
          |  "postalCode": "69042",
          |  "city": "Apéro-Sur-Le Coudonzeur",
          |  "country": "France"
        }""".stripMargin

      val validated = Json.parse(inputJson).validate[Customer]

      validated should matchPattern {
        case JsSuccess(_, _) =>
      }

      validated.get should be(Customer("Laurent", "Outang", Address("3 Bis", "rue Pin", "69042", "Apéro-Sur-Le Coudonzeur", None, "France")))
    }

    it("should parse a JSON with Address attributes at top-level and a defined state"){
      val inputJson =
        """{
          |  "firstName": "Paul",
          |  "lastName": "Dance",
          |  "number": "1337",
          |  "street": "John F. Kennedy avenue",
          |  "postalCode": "42000",
          |  "city": "Dallas",
          |  "country": "USA",
          |  "state": "Texas"
        }""".stripMargin

      val validated = Json.parse(inputJson).validate[Customer]

      validated should matchPattern {
        case JsSuccess(_, _) =>
      }

      validated.get should be(Customer("Paul", "Dance", Address("1337", "John F. Kennedy avenue", "42000", "Dallas", Some("Texas"), "USA")))
    }

  }
}
