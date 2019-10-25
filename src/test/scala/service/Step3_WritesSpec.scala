package service

import models.{Discount, Invoice, InvoiceLine}
import models.Discount.writes
import models.Invoice.writes
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class Step3_WritesSpec extends WordSpec with Matchers {
	private final val jsonInvoiceLineWithDiscount = Json.parse("""{ "product": "MacBook Pro mid-2019", "quantity": 5, "unitPrice": 2799, "discount": {"label": "Black Friday", "value": 0.05}}""")
	private final val invoiceLineWithDiscount = InvoiceLine("MacBook Pro mid-2019", Option(Discount("Black Friday", BigDecimal("0.05"))), 5, 2799)
	private final val jsonInvoiceLineWithoutDiscount = Json.parse("""{ "product": "MacBook Pro mid-2019", "quantity": 5, "unitPrice": 2799 }""")
	private final val invoiceLineWithoutDiscount = InvoiceLine("MacBook Pro mid-2019", None, 5, 2799)
	private final val jsonInvoice = Json.parse(
		s"""{
		  |  "lines": [
		  |    ${jsonInvoiceLineWithoutDiscount},
		  |    $jsonInvoiceLineWithDiscount
		  |  ]
		  |}""".stripMargin
	)

	"A Discount" should {
		"be rendered as JSON" in {
			Json.toJson(Discount("Family discount", BigDecimal(0.21))) shouldBe Json.parse("""{"label": "Family discount", "value": 0.21}""")
		}
	}

	"An InvoiceLine" when {
		"containing a discount" should {
			"be rendered as JSON" in {
				Json.toJson(invoiceLineWithDiscount) shouldBe jsonInvoiceLineWithDiscount
			}
		}
		"not containing any discount" should {
			"be rendered as JSON" in {
				Json.toJson(invoiceLineWithoutDiscount) shouldBe jsonInvoiceLineWithoutDiscount
			}
		}
	}

	"An Invoice" should {
		"be rendered as JSON" in {
			Json.toJson(Invoice(List(invoiceLineWithoutDiscount, invoiceLineWithDiscount))) shouldBe jsonInvoice
		}
	}

	//Looking for tests about Movie ? See models.MovieSpec
}
