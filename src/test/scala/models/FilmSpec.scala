package models

import scala.BigDecimal
import org.scalatest.{FunSpec, Inside, Matchers}
import play.api.libs.json._

class FilmSpec extends FunSpec with Matchers with Inside {
  val spaceballTypes = Seq(FilmType.Comedy, FilmType.SciFi)
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
          |  "author": "Mel Brooks",
		  |  "types": ["comedy", "sci-fi"]
          |}""".stripMargin

      val validatedInput = Json.parse(jsonInput).validate[Film]
      inside(validatedInput) {
        case JsSuccess(value, _) => value should be(Film(1400000000L, "Spaceballs", Json.obj(
          "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
          "notes" -> "Mel Brooks and Rick Moranis in a delirium"
        ), Some(Author("Mel Brooks")), spaceballTypes))
      }
    }
  }

  it("should read a JSON without Author, empty object as additional infos and an empty type list"){
    val jsonInput =
      """{
        | "startTimestamp": 1400000000,
        | "name": "Spaceballs",
        | "additionalInfo": {}
        |}""".stripMargin

    inside(Json.parse(jsonInput).validate[Film]) {
      case JsSuccess(value, _) => value should be(Film(1400000000L, "Spaceballs", Json.obj(), None, Seq.empty))
    }
    /* Just for example: instead of "validate" (that is returning an Either-like functor), we can use "as" (that is returning the right type but
       raising exception in case of error)
     */
    Json.parse(jsonInput).as[Film] should be(Film(1400000000L, "Spaceballs", Json.obj(), None, Seq.empty))
  }

  it("should read a JSON with additionalInfo, when the type of JSON data is other than object"){
    val jsonInputWithString =
      """{
        | "startTimestamp": 1400000000,
        | "name": "Spaceballs",
        | "additionalInfo": "Nice",
        | "author": "Mel Brooks"
        |}""".stripMargin

    val jsonInputWithArray =
      """{
        | "startTimestamp": 1400000000,
        | "name": "Spaceballs",
        | "additionalInfo": [42, 1337, "Bim !"],
        | "author": "Mel Brooks"
        |}""".stripMargin

    val jsonInputWithNull =
      """{
        | "startTimestamp": 1400000000,
        | "name": "Spaceballs",
        | "additionalInfo": null,
        | "author": "Mel Brooks"
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

