package models

import org.scalatest.{FunSpec, ShouldMatchers}
import play.api.libs.json.{JsArray, JsString, JsSuccess, Json}

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

      Json.parse(jsonInput).validate[Film] should matchPattern {
        case JsSuccess(value, path) =>
      }
      Json.parse(jsonInput).as[Film] should be(Film(1400000000L, "Spaceballs", Json.obj(
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
}

