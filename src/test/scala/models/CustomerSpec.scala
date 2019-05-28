package models

import org.scalatest.{Inside, _}
import play.api.libs.json.{JsError, JsObject, JsPath, JsString, JsSuccess, Json, JsonValidationError, KeyPathNode}

class CustomerSpec extends FunSpec with Matchers with Inside {
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
        case JsSuccess(Customer("Laurent", "Outang", Address(AddressNumber("3 Bis"), Street("rue Pin"), PostalCode("69042"), "Apéro-Sur-Le Coudonzeur", None, "France")), _) =>
      }
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
        case JsSuccess(Customer("Paul", "Dance", Address(AddressNumber("1337"), Street("John F. Kennedy avenue"), PostalCode("42000"), "Dallas", Some("Texas"), "USA")), _) =>
      }
    }

    it("should be written as JSON with Address attributes at top-level and no state") {
      val expectedJson =
        """{
          |  "firstName": "Laurent",
          |  "lastName": "Outang",
          |  "number": "3 Bis",
          |  "street": "rue Pin",
          |  "postalCode": "69042",
          |  "city": "Apéro-Sur-Le Coudonzeur",
          |  "country": "France"
        }""".stripMargin
      val expectedJsObject = Json.parse(expectedJson).as[JsObject]
      AddressNumber("3 Bis").map { n =>
          Json.toJson(
              Customer("Laurent", "Outang", Address(n, "rue Pin", "69042", "Apéro-Sur-Le Coudonzeur", None, "France"))
          )
      } should contain(expectedJsObject)

    }

    it("should be written as JSON with Address attributes at top-level and a defined state") {
      val expectedJson =
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
      val expectedJsObject = Json.parse(expectedJson).as[JsObject]

      AddressNumber("1337").map { n =>
          Json.toJson(
              Customer("Paul", "Dance", Address(n, "John F. Kennedy avenue", "42000", "Dallas", Some("Texas"), "USA"))
          )
      } should contain(expectedJsObject)

    }

    describe("address"){

      it("should read a JSON without state ") {
        val inputJson =
          """{
            |  "number": "3 Bis",
            |  "street": "rue Pin",
            |  "postalCode": "69042",
            |  "city": "Apéro-Sur-Le Coudonzeur",
            |  "country": "France"
        }""".stripMargin

        val validated = Json.parse(inputJson).validate[Address]

        validated should matchPattern {
          case JsSuccess(Address(AddressNumber("3 Bis"), Street("rue Pin"), PostalCode("69042"), "Apéro-Sur-Le Coudonzeur", None, "France"), _) =>
        }
      }

      it("should read a JSON with defined state ") {
        val inputJson =
          """{
            |  "number": "1337",
            |  "street": "John F. Kennedy avenue",
            |  "postalCode": "42000",
            |  "city": "Dallas",
            |  "country": "USA",
            |  "state": "Texas"
        }""".stripMargin

        val validated = Json.parse(inputJson).validate[Address]

        validated should matchPattern {
          case JsSuccess(Address(AddressNumber("1337"), Street("John F. Kennedy avenue"), PostalCode("42000"), "Dallas", Some("Texas"), "USA"), _) =>
        }
      }

      it("should raise an error if address number is invalid") {
          val inputJson = """{
                          |  "number": "leet",
                          |  "street": "John F. Kennedy avenue",
                          |  "postalCode": "42000",
                          |  "city": "Dallas",
                          |  "country": "USA",
                          |  "state": "Texas"
                          |}""".stripMargin

          val validated = Json.parse(inputJson).validate[Address]

          validated should matchPattern {
              case JsError(List(
                (JsPath(KeyPathNode("number") :: Nil), List(JsonValidationError(List("string.not.matching.addressnumber"), _*)))
              )) =>
          }
      }

      it("should be writen as JSON without state") {
        val expectedJson =
          """{
            |  "number": "3 Bis",
            |  "street": "rue Pin",
            |  "postalCode": "69042",
            |  "city": "Apéro-Sur-Le Coudonzeur",
            |  "country": "France"
        }""".stripMargin
        val expectedJsObject = Json.parse(expectedJson).as[JsObject]

        AddressNumber("3 Bis").map {n =>
            Json.toJson(
                Address(n, "rue Pin", "69042", "Apéro-Sur-Le Coudonzeur", None, "France")
            )
        }should contain(expectedJsObject)
      }

      it("should be writen as JSON with a defined state") {
        val expectedJson =
          """{
            |  "number": "1337",
            |  "street": "John F. Kennedy avenue",
            |  "postalCode": "42000",
            |  "city": "Dallas",
            |  "country": "USA",
            |  "state": "Texas"
        }""".stripMargin
        val expectedJsObject = Json.parse(expectedJson).as[JsObject]

        AddressNumber("1337").map { n =>
            Json.toJson(
                Address(n, "John F. Kennedy avenue", "42000", "Dallas", Some("Texas"), "USA")
            )
        } should contain(expectedJsObject)
      }

      describe("number") {
        it("should read a JSON string"){
          Json.parse(
            """
              |"3 Bis"
            """.stripMargin).validate[AddressNumber] should matchPattern{
              case JsSuccess(AddressNumber("3 Bis"), _) =>
          }
        }

        it("should be written as a JSON string"){
          Json.toJson(AddressNumber("3 Bis")) should be(JsString("3 Bis"))
        }
      }

      describe("street") {
        it("should read a JSON string"){
          Json.parse(
            """
              |"rue Tabaga"
            """.stripMargin).asOpt[Street] should contain(Street("rue Tabaga"))
        }

        it("should be written as a JSON string"){
          Json.toJson(Street("rue Tabaga")) should be(JsString("rue Tabaga"))
        }
      }

      describe("postal code") {
        it("should read a JSON string"){
          Json.parse(
            """
              |"93370"
            """.stripMargin).asOpt[PostalCode] should contain(PostalCode("93370"))
        }
        it("should be written as a JSON string"){
          Json.toJson(PostalCode("75008")) should be(JsString("75008"))
        }
      }

    }

  }
}
