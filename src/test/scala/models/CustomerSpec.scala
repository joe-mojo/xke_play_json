package models

import org.scalatest.{Inside, _}
import play.api.libs.json.{JsObject, JsString, JsSuccess, Json}

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

      Json.toJson(
        Customer("Laurent", "Outang", Address("3 Bis", "rue Pin", "69042", "Apéro-Sur-Le Coudonzeur", None, "France"))
      ) should be(expectedJsObject)

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

      Json.toJson(
        Customer("Paul", "Dance", Address("1337", "John F. Kennedy avenue", "42000", "Dallas", Some("Texas"), "USA"))
      ) should be(expectedJsObject)

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
          case JsSuccess(_, _) =>
        }

        validated.get should be(Address("3 Bis", "rue Pin", "69042", "Apéro-Sur-Le Coudonzeur", None, "France"))
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
          case JsSuccess(_, _) =>
        }

        validated.get should be(Address("1337", "John F. Kennedy avenue", "42000", "Dallas", Some("Texas"), "USA"))
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

        Json.toJson(
          Address("3 Bis", "rue Pin", "69042", "Apéro-Sur-Le Coudonzeur", None, "France")
        ) should  be(expectedJsObject)
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

        Json.toJson(
          Address("1337", "John F. Kennedy avenue", "42000", "Dallas", Some("Texas"), "USA")
        ) should  be(expectedJsObject)
      }

      describe("number") {
        it("should read a JSON string"){
          Json.parse(
            """
              |"3 Bis"
            """.stripMargin).asOpt[AddressNumber] should contain(AddressNumber("3 Bis"))
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
