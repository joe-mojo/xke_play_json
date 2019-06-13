package models

import org.scalatest.{FunSpec, Inside, Matchers}
import play.api.libs.json.{JsSuccess, Json}

class InvoiceSpec extends FunSpec with Matchers with Inside {
  private final val jsonInvoiceLineWithDiscount = """{"product": "123", "quantity": 2, "unitPrice": 149, "discount": { "label": "Offre exceptionelle", "value": 0.2}}"""
  private final val jsonInvoiceLineWithoutDiscount = """{"product": "456", "quantity": 1, "unitPrice": 15.2}"""
  private final val jsonInvoice =
    s"""{
      |"lines": [
      |  $jsonInvoiceLineWithDiscount,
      |  $jsonInvoiceLineWithoutDiscount
      |]
      |}""".stripMargin
  val invoiceLineWithDiscount = InvoiceLine("123", Some(Discount("Offre exceptionelle", 0.2D)), 2, 149) //WARNING Here we are lucky that 0.2D is an exact Double value.
  val invoiceLineWithoutDiscount = InvoiceLine("456", None, 1, BigDecimal("15.2"))
  val invoice = Invoice(List(invoiceLineWithDiscount, invoiceLineWithoutDiscount))

  describe("A JSON of discount") {
    it("should be parsed as Discount") {
      inside(Json.parse("""{"label": "Last sales before brexit!", "value": 0.30}""").validate[Discount]) {
        case JsSuccess(Discount("Last sales before brexit!", value), path) => value shouldBe BigDecimal("0.3")
      }
    }
  }

  describe("A JSON of invoice line") {

    it("with discount should be parsed InvoiceLine with Discount") {
      Json.parse(jsonInvoiceLineWithDiscount).validate[InvoiceLine] should matchPattern {
        case JsSuccess(`invoiceLineWithDiscount`, path) =>
      }
    }

    it("without discount should be parsed as InvoiceLine without Dioscount") {
      Json.parse(jsonInvoiceLineWithoutDiscount).validate[InvoiceLine] should matchPattern {
        case JsSuccess(`invoiceLineWithoutDiscount`, path) =>
      }
    }

  }

  describe("A JSON of invoice") {
    it("should be parsed as Invoice") {
      Json.parse(jsonInvoice).validate[Invoice] should matchPattern {
        case JsSuccess(`invoice`, path) =>
      }
    }
  }

}
