package models

import org.scalatest.{FunSpec, Inside, ShouldMatchers}
import play.api.libs.json._

class FilmEventSpec extends FunSpec with ShouldMatchers with Inside{
  describe("A FilmEvent") {
    it("""should parse a JSON film event with "created" type and "film" data type""") {
      val jsonInput =
        """
          | {
          |   "eventType": "created",
          |   "dataType": "film",
          |   "data": {
          |     "startTimestamp": 1400000000,
          |     "name": "Spaceballs",
          |     "additionalInfo": {
          |       "metas": ["fun", "parody"],
          |       "notes": "Mel Brooks and Rick Moranis in a delirium"
          |     },
          |     "author": "Mel Brooks"
          |   }
          | }
        """.stripMargin

      inside(Json.parse(jsonInput).validate[FilmCreated])  {
        case JsSuccess(filmEvt, path) =>
          filmEvt should be(FilmCreated(Film(1400000000L, "Spaceballs", Json.obj(
            "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
            "notes" -> "Mel Brooks and Rick Moranis in a delirium"
          ), Some(Author("Mel Brooks")))))
      }

    }

    it("""should not parse a JSON film event with a type other than "created"""") {
      val jsonInput =
        """
          | {
          |   "eventType": "updated",
          |   "dataType": "film",
          |   "data": {
          |     "startTimestamp": 1400000000,
          |     "name": "Spaceballs",
          |     "additionalInfo": {
          |       "metas": ["fun", "parody"],
          |       "notes": "Mel Brooks and Rick Moranis in a delirium"
          |     },
          |     "author": "Mel Brooks"
          |   }
          | }
        """.stripMargin

      Json.parse(jsonInput).validate[FilmCreated] should matchPattern {
        case JsError(errors) =>
      }
    }

    it("""should not parse a JSON film event with "created" type but wrong dataType """) {
      val jsonInput =
        """
          | {
          |   "eventType": "created",
          |   "dataType": "invoice",
          |   "data": {
          |     "startTimestamp": 1400000000,
          |     "name": "Spaceballs",
          |     "additionalInfo": {
          |       "metas": ["fun", "parody"],
          |       "notes": "Mel Brooks and Rick Moranis in a delirium"
          |     },
          |     "author": "Mel Brooks"
          |   }
          | }
        """.stripMargin

      Json.parse(jsonInput).validate[FilmCreated] should matchPattern {
        case JsError(errors) =>
      }
    }
  }

}
