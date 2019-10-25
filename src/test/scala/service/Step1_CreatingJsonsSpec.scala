package service

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.{JsNumber, JsObject, JsString, Json}
import service.Step1_CreatingJsons._


class Step1_CreatingJsonsSpec extends WordSpec with Matchers {

	"parseSnatch" should {
		"parse a string with Movie JSON" in {
			parseSnatch should be {
				Json.obj(
					"timestamp" -> JsNumber(BigDecimal(974271600000L)),
					"name" -> JsString("Snatch"),
					"author" -> JsString("Guy Ritchie"),
					"additionalInfo" -> Json.obj(
						"imdbScore" -> JsNumber(BigDecimal(8.3))
					)
				)
			}
		}
	}
	"createYourJson" should {
		"""create a JSON object equivalent to "type": "Point", "coordinates": [2.3088449, 48.8753487]}""" in {
			createYourJson should be {
				Json.parse("""{"type": "Point", "coordinates": [2.3088449, 48.8753487]}""")
			}
		}
	}

	"checkThisJsonMovie" when {
		"passed a valid Json" should {
			"return (true, true)" in {
				checkThisJsonMovie(Json.parse("""{"name" : "Snatch", "additionalInfo": {"imdbScore": 8.3}}""").as[JsObject]) should be(true -> true)
				checkThisJsonMovie(Json.parse("""{"name" : "A", "additionalInfo": {"imdbScore": 10}}""").as[JsObject]) should be(true -> true)
				checkThisJsonMovie(Json.parse("""{"name" : "A", "additionalInfo": {"imdbScore": "10"}}""").as[JsObject]) should be(true -> true)
				checkThisJsonMovie(Json.parse("""{"name" : "1", "additionalInfo": {"imdbScore": 0}}""").as[JsObject]) should be(true -> true)
			}
		}
		"passed a Json with invalid name but invalid imdbScore" should {
			"return (false, false)" in {
				checkThisJsonMovie(Json.parse("""{"name" : 12, "additionalInfo": {"imdbScore": 11}}""").as[JsObject]) should be(false -> false)
				checkThisJsonMovie(Json.parse("""{"name" : ["A"], "additionalInfo": {"imdbScore": "_10"}}""").as[JsObject]) should be(false -> false)
				checkThisJsonMovie(Json.parse("""{"name" : {"name": "Toto"}, "additionalInfo": {"imdbScore": -0.05}}""").as[JsObject]) should be(false -> false)
				checkThisJsonMovie(Json.parse("""{"name" : null, "additionalInfo": {"score": 0}}""").as[JsObject]) should be(false -> false)
				checkThisJsonMovie(Json.parse("""{"additionalInfo": {"imdbScore": [5]}}""").as[JsObject]) should be(false -> false)
				checkThisJsonMovie(Json.parse("""{"imdbScore": 5}""").as[JsObject]) should be(false -> false)
			}
		}
	}

	"checkImdbScore" when {
		"passed a valid number " should {
			"return true" in {
				checkImdbScore(Json.parse("""{"additionalInfo": {"imdbScore": 8.3}}""").as[JsObject]) shouldBe true
				checkImdbScore(Json.parse("""{"additionalInfo": {"imdbScore": 0.05}}""").as[JsObject]) shouldBe true
				checkImdbScore(Json.parse("""{"additionalInfo": {"imdbScore": 9.95}}""").as[JsObject]) shouldBe true
			}
		}
		"passed an invalid number" should {
			"return false" in {
				checkImdbScore(Json.parse("""{"additionalInfo": {"imdbScore": 10.01}}""").as[JsObject]) shouldBe false
				checkImdbScore(Json.parse("""{"additionalInfo": {"imdbScore": -0.01}}""").as[JsObject]) shouldBe false
				checkImdbScore(Json.parse("""{"additionalInfo": {"imdbScore": -5}}""").as[JsObject]) shouldBe false
				checkImdbScore(Json.parse("""{"additionalInfo": {"imdbScore": 30}}""").as[JsObject]) shouldBe false
			}
		}
		"passed a string or whatever" should {
			"return false" in {
				checkImdbScore(Json.parse("""{"additionalInfo": {"imdbScore": "5"}}""").as[JsObject]) shouldBe false
				checkImdbScore(Json.parse("""{"additionalInfo": {"imdbScore": [5]}}""").as[JsObject]) shouldBe false
				checkImdbScore(Json.parse("""{"additionalInfo": {"imdbScore": {"value": 5}}}""").as[JsObject]) shouldBe false
			}
		}
	}

}
