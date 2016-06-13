package models

import org.scalatest.{FunSpec, ShouldMatchers}
import play.api.libs.json.{JsSuccess, Json}

class InvoiceSpec extends FunSpec with ShouldMatchers {
  describe("An Invoice") {
    it("should parse invoice line with a discount") {
      val jsonInput = """{
                         |  "product": "123",
                         |  "quantity": 2,
                         |  "unitPrice": 149,
                         |  "discount": {
                         |    "label": "Offre exceptionelle",
                         |    "value": 0.2
                         |  }
                         |}""".stripMargin

      Json.parse(jsonInput).validate[InvoiceLine] should matchPattern {
        case JsSuccess(value, path) =>
      }
      Json.parse(jsonInput).as[InvoiceLine] should be(InvoiceLine("123", Some(Discount("Offre exceptionelle", 0.2D)), 2, 149))

    }

    it("should parse invoice line without discount") {
      val jsonInput = """{
                        |  "product": "123",
                        |  "quantity": 2,
                        |  "unitPrice": 149
                        |}""".stripMargin

      Json.parse(jsonInput).validate[InvoiceLine] should matchPattern {
        case JsSuccess(value, path) =>
      }
      Json.parse(jsonInput).as[InvoiceLine] should be(InvoiceLine("123", None, 2, 149))
    }
  }
}
