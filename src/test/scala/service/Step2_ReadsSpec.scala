package service

import models.FilmType.{Comedy, Drama, Horror}
import models.{Address, AddressNumber, Author, Discount, Film, FilmType, InvoiceLine, PostalCode, Street}
import org.scalatest.{Inside, Matchers, WordSpec}
import play.api.libs.json._

import scala.math.BigDecimal

class Step2_ReadsSpec extends WordSpec with Matchers with Inside{

	"A Reads[Discount]" when {
		import models.Discount.reads
		"parsing a valid JSON" should {
			"validate it as Discount" in {
				inside(Json.parse("""{ "label": "Family discount", "value": 0.25 }""").validate[Discount]) {
					case JsSuccess(Discount("Family discount", value), _) => value shouldBe BigDecimal("0.25")
				}
				inside(Json.parse("""{ "label": "Unemployed", "value": 0.5 }""").validate[Discount]) {
					case JsSuccess(Discount("Unemployed", value), _) => value shouldBe BigDecimal("0.5")
				}
				inside(Json.parse("""{ "label": "Winter sales", "value": 0.6, "useless": "attribute" }""").validate[Discount]) {
					case JsSuccess(Discount("Winter sales", value), _) => value shouldBe BigDecimal("0.6")
				}
			}
		}
		"parsing an invalid JSON" should {
			"return a error" in {
				val valueErrPath: JsPath = JsPath \ "value"
				Json.parse("""{ "label": "Family discount"}""").validate[Discount] should matchPattern {
					case JsError(List(
						(`valueErrPath`, List(JsonValidationError(List("error.path.missing"), _*)))
					)) =>
				}
				Json.parse("""{ "label": "Family discount", "value": "A"}""").validate[Discount] should matchPattern {
					case JsError(List(
					(`valueErrPath`, List(JsonValidationError(List("error.expected.numberformatexception"), _*)))
					)) =>
				}

				val labelErrPath = JsPath \ "label"
				Json.parse("""{ "value": 0.25}""").validate[Discount] should matchPattern {
					case JsError(List(
						(`labelErrPath`, List(JsonValidationError(List("error.path.missing"), _*)))
					)) =>
				}
				Json.parse("""{"label": ["Family discount"], "value": 0.25}""").validate[Discount] should matchPattern {
					case JsError(List(
					(`labelErrPath`, List(JsonValidationError(List("error.expected.jsstring"), _*)))
					)) =>
				}
			}
		}
	}

	"A Reads[InvoiceLine]" when {
		import models.InvoiceLine
		"parsing a valid JSON" should {
			"validate it as InvoiceLine" in {
				inside(Json.parse("""{ "product": "MacBook Pro mid-2019", "quantity": 5, "unitPrice": 2799 }""").validate[InvoiceLine]) {
					case JsSuccess(InvoiceLine("MacBook Pro mid-2019", None, 5, unitPrice), _) => unitPrice shouldBe BigDecimal(2799)
				}
				inside(Json.parse("""{ "product": "MacBook Pro mid-2019", "quantity": 5, "unitPrice": 2799, "discount": {"label": "Black Friday", "value": 0.05}}""").validate[InvoiceLine]) {
					case JsSuccess(InvoiceLine("MacBook Pro mid-2019", Some(Discount("Black Friday", discountValue)), 5, unitPrice), _) =>
						unitPrice shouldBe BigDecimal(2799)
						discountValue shouldBe BigDecimal("0.05")
				}
			}
		}
	}

	"A Reads[Invoice]" when {
		import models.Invoice
		"parsing a valid JSON" should {
			"validate it as InvoiceLine" in {
				inside(Json.parse(
					"""
					  |{
					  |  "lines": [
					  |    { "product": "MacBook Pro mid-2019 15\"", "quantity": 5, "unitPrice": 2799 },
					  |    { "product": "MacBook Pro mid-2019 15\" 32Go i9", "quantity": 1, "unitPrice": 3639, "discount": {"label": "Black Friday", "value": 0.05}}
					  |  ]
					  |}
					""".stripMargin).validate[Invoice]) {
					case JsSuccess(Invoice(Seq(
						InvoiceLine("""MacBook Pro mid-2019 15"""", None, 5, price1),
						InvoiceLine("""MacBook Pro mid-2019 15" 32Go i9""", Some(Discount("Black Friday", discountValue)), 1, price2)
					)), _) =>
						price1 shouldBe BigDecimal(2799)
						discountValue shouldBe BigDecimal("0.05")
						price2 shouldBe BigDecimal(3639)
				}
			}
		}
	}

	"A Reads[Film]" when {
		import models.Film.reads
		"parsing a valid JSON without author" should {
			"validate it as Film" in {
				Json.parse("""{"startTimestamp": 974271600000, "name": "Snatch", "additionalInfo" : "Whatever you want"}""").validate[Film] should matchPattern {
					case JsSuccess(Film(974271600000L, "Snatch", JsString("Whatever you want"), None), _) =>
				}
			}
		}
		"parsing a valid JSON with author" should {
			"validate it as Film" in {
				inside(Json.parse(
					"""{"startTimestamp": 974271600000, "name": "Snatch", "additionalInfo" : {"whatever": "you want"}, "author": "Guy Richie"}"""
				).validate[Film]){
					case JsSuccess(Film(974271600000L, "Snatch", additionalInfo, Some(Author("Guy Richie"))), _) =>
						additionalInfo shouldBe Json.obj("whatever" -> JsString("you want"))
				}

			}
		}
	}

	"A Reads[FilmType]" when {
		"parsing a valid JSON" should {
			"validate it as FilmType" in {
				Json.parse(""""horror"""").validate[FilmType] should matchPattern {
					case JsSuccess(Horror, _) =>
				}
				Json.parse(""""drama"""").validate[FilmType] should matchPattern {
					case JsSuccess(Drama, _) =>
				}
				Json.parse(""""comedy"""").validate[FilmType] should matchPattern {
					case JsSuccess(Comedy, _) =>
				}

			}
		}
	}

	"A Reads[PostalCode]" when {
		"parsing a valid JSON string" should {
			"validate it as PostalCode" in {
				Json.parse(""""93270"""").validate[PostalCode] should matchPattern {
					case JsSuccess(PostalCode("93270"), _) =>
				}
			}
		}
	}
	"A Reads[Street]" when {
		"parsing a valid JSON string" should {
			"validate it as Street" in {
				Json.parse(""""allée Paul Fort"""").validate[Street] should matchPattern {
					case JsSuccess(Street("allée Paul Fort"), _) =>
				}
			}
		}
	}
	"A Reads[StreetNumber]" when {
		"parsing a valid address number in a valid JSON string" should {
			"validate it as AddressNumber" in {
				Json.parse(""""18"""").validate[AddressNumber] should matchPattern {
					case JsSuccess(AddressNumber("18"), _) =>
				}
				Json.parse(""""2 Bis"""").validate[AddressNumber] should matchPattern {
					case JsSuccess(AddressNumber("2 Bis"), _) =>
				}
				Json.parse(""""26 Ter"""").validate[AddressNumber] should matchPattern {
					case JsSuccess(AddressNumber("26 Ter"), _) =>
				}
				Json.parse(""""10-16"""").validate[AddressNumber] should matchPattern {
					case JsSuccess(AddressNumber("10-16"), _) =>
				}
			}
		}
		"parsing an invalid address number in a valid JSON string" should {
			"return a JsError" in {
				Json.parse(""" "2 Bis-3 Ter" """).validate[AddressNumber] should matchPattern {
					case JsError(List(
					(JsPath(Nil), List(JsonValidationError(List("string.not.matching.addressnumber"), _*)))
					)) =>
				}
				Json.parse(""" "F6" """).validate[AddressNumber] should matchPattern {
					case JsError(List(
					(JsPath(Nil), List(JsonValidationError(List("string.not.matching.addressnumber"), _*)))
					)) =>
				}
			}
		}
	}

}
