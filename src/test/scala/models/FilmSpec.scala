package models

import scala.BigDecimal

import org.scalatest.{FunSpec, ShouldMatchers}
import play.api.libs.json._

class FilmSpec extends FunSpec with ShouldMatchers{
  describe("A Film"){
    it("should read a simple JSON"){
      val jsonInput =
        """{
          | "startTimestamp": 1400000000,
          | "name": "Spaceballs",
          | "additionalInfo": {
          |     "metas": ["fun", "parody"],
          |     "notes": "Mel Brooks and Rick Moranis in a delirium"
          |   },
          |  "author": "Mel Brooks"
          |}""".stripMargin

      val validatedInput = Json.parse(jsonInput).validate[Film]
      validatedInput should matchPattern {
        case JsSuccess(value, path) =>
      }
      validatedInput.get should be(Film(1400000000L, "Spaceballs", Json.obj(
        "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
        "notes" -> "Mel Brooks and Rick Moranis in a delirium"
      ), Some(Author("Mel Brooks"))))
    }
  }

  it("should read a JSON without Author and empty object as additional infos"){
    val jsonInput =
      """{
        | "startTimestamp": 1400000000,
        | "name": "Spaceballs",
        | "additionalInfo": {}
        |}""".stripMargin

    Json.parse(jsonInput).validate[Film] should matchPattern {
      case JsSuccess(value, path) =>
    }
    Json.parse(jsonInput).as[Film] should be(Film(1400000000L, "Spaceballs", Json.obj(), None))
  }

  it("should read a JSON with additionalInfo, when the type of JSON data is other than object"){
    val jsonInputWithString =
      """{
        | "startTimestamp": 1400000000,
        | "name": "Spaceballs",
        | "additionalInfo": "Nice",
        |  "author": "Mel Brooks"
        |}""".stripMargin

    val jsonInputWithArray =
      """{
        | "startTimestamp": 1400000000,
        | "name": "Spaceballs",
        | "additionalInfo": [42, 1337, "Bim !"],
        |  "author": "Mel Brooks"
        |}""".stripMargin

    val jsonInputWithNull =
      """{
        | "startTimestamp": 1400000000,
        | "name": "Spaceballs",
        | "additionalInfo": null,
        |  "author": "Mel Brooks"
        |}""".stripMargin

    val validatedInputs = Seq(jsonInputWithArray, jsonInputWithNull, jsonInputWithString)
        .map(Json.parse).map(_.validate[Film])

    validatedInputs should matchPattern {
      case JsSuccess(v1, p1) :: JsSuccess(v2, p2) :: JsSuccess(v3, p3) :: Nil =>
    }

    val films = validatedInputs.map(_.get)
    films.foreach {film =>
      film.timestamp should be(1400000000)
      film.name should be("Spaceballs")
      film.author should be(Some(Author("Mel Brooks")))
    }

    films.head.additionalInfo should matchPattern {
      case JsArray(Seq(JsNumber(n), JsNumber(m), JsString("Bim !"))) if n == BigDecimal(42) && m == BigDecimal(1337) =>
    }

    films(1).additionalInfo should matchPattern {
      case JsNull =>
    }


    films.last.additionalInfo should matchPattern {
      case JsString("Nice") =>
    }

  }
}

