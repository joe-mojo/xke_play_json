package models

import org.scalatest.{FunSpec, Inside, Matchers}
import play.api.libs.json._

class FilmEventSpec extends FunSpec with Matchers with Inside{
  private final val validJsonOfFilmCreated =
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
  private final val validJsonOfFilmUpdated =
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
  private final val validJsonOfFilmDeleted =
    """
      | {
      |   "eventType": "deleted",
      |   "dataType": "film",
      |   "data": {
      |     "startTimestamp": 1400000000,
      |     "name": "Spaceballs",
      |     "author": "Mel Brooks",
      |     "additionalInfo": null
      |   }
      | }
    """.stripMargin
  private final val invalidJsonOfFilmCreated =
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
  private final val invalidJsonOfFilmEvent =
    """
      | {
      |   "eventType": "schtroumpfed",
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
  private final val malformedDataJson = """
    | {
    |   "eventType": "created",
    |   "dataType": "film",
    |   "data": {
    |     "startTimestamp": 1400000000,
    |     "name": "Spaceballs",
    |     "author": "Mel Brooks"
    |   }
    | }
  """.stripMargin

  describe("A FilmCreated event") {
    it("""should parse a JSON film event with "created" type and "film" data type""") {
      inside(Json.parse(validJsonOfFilmCreated).validate[FilmCreated])  {
        case JsSuccess(filmEvt, path) =>
          filmEvt should be(FilmCreated(Film(1400000000L, "Spaceballs", Json.obj(
            "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
            "notes" -> "Mel Brooks and Rick Moranis in a delirium"
          ), Some(Author("Mel Brooks")))))
      }
    }

    it("""should render a FilmCreated as a valid JSON""") {
      Json.toJson(FilmCreated(Film(1400000000L, "Spaceballs", Json.obj(
        "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
        "notes" -> "Mel Brooks and Rick Moranis in a delirium"
      ), Some(Author("Mel Brooks")))))(FilmCreated.writes) shouldBe Json.parse(validJsonOfFilmCreated)
    }

    it("""should render a FilmUpdated as a valid JSON""") {
      Json.toJson(FilmUpdated(Film(1400000000L, "Spaceballs", Json.obj(
        "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
        "notes" -> "Mel Brooks and Rick Moranis in a delirium"
      ), Some(Author("Mel Brooks")))))(FilmUpdated.writes) shouldBe Json.parse(validJsonOfFilmUpdated)
    }

    it("""should render a FilmDeleted as a valid JSON""") {
      Json.toJson(
        FilmDeleted(Film(1400000000L, "Spaceballs", JsNull, Some(Author("Mel Brooks"))))
      )(FilmDeleted.writes) shouldBe Json.parse(validJsonOfFilmDeleted)
    }

    it("""should not parse a JSON film event with a type other than "created"""") {
      Json.parse(validJsonOfFilmUpdated).validate[FilmCreated] should matchPattern {
        case JsError(errors) =>
      }
    }

    it("""should not parse a JSON film event with "created" type but wrong dataType""") {
      Json.parse(invalidJsonOfFilmCreated).validate[FilmCreated] should matchPattern {
        case JsError(errors) =>
      }
    }
  }

  describe("A FilmEvent") {
    describe("using only FilmEvent trait") {
      it("""should parse a "created" JSON film event""") {
        inside(Json.parse(validJsonOfFilmCreated).validate[FilmEvent]) {
          case JsSuccess(FilmCreated(Film(1400000000L, "Spaceballs", infos: JsObject, Some(Author("Mel Brooks")))), _) =>
            inside((infos \ "metas").validate[Array[String]]) {
              case JsSuccess(Array("fun", "parody"), _) =>
            }
            (infos \ "notes").asOpt[String] should contain("Mel Brooks and Rick Moranis in a delirium")
            infos.keys should contain theSameElementsAs Seq("metas", "notes")
        }
      }
      it("""should render a "created" JSON film event""") {
        Json.toJson(FilmCreated(Film(1400000000L, "Spaceballs", Json.obj(
          "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
          "notes" -> "Mel Brooks and Rick Moranis in a delirium"
        ), Some(Author("Mel Brooks")))))(FilmEvent.writes) shouldBe Json.parse(validJsonOfFilmCreated)
      }

      it("""should parse an "updated" JSON film event""") {
        inside(Json.parse(validJsonOfFilmUpdated).validate[FilmEvent]) {
          case JsSuccess(FilmUpdated(Film(1400000000L, "Spaceballs", infos: JsObject, Some(Author("Mel Brooks")))), _) =>
            inside((infos \ "metas").validate[Array[String]]) {
              case JsSuccess(Array("fun", "parody"), _) =>
            }
            (infos \ "notes").asOpt[String] should contain("Mel Brooks and Rick Moranis in a delirium")
            infos.keys should contain theSameElementsAs Seq("metas", "notes")
        }
      }
      it("""should render an "updated" JSON film event""") {
        Json.toJson(FilmUpdated(Film(1400000000L, "Spaceballs", Json.obj(
          "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
          "notes" -> "Mel Brooks and Rick Moranis in a delirium"
        ), Some(Author("Mel Brooks")))))(FilmEvent.writes) shouldBe Json.parse(validJsonOfFilmUpdated)
      }

      it("""should parse a "deleted" JSON film event""") {
        Json.parse(validJsonOfFilmDeleted).validate[FilmEvent] should matchPattern {
          case JsSuccess(FilmDeleted(Film(1400000000L, "Spaceballs", JsNull, Some(Author("Mel Brooks")))), _) =>
        }
      }
      it("""should render a "deleted" JSON film event""") {
        Json.toJson(
          FilmDeleted(Film(1400000000L, "Spaceballs", JsNull, Some(Author("Mel Brooks"))))
        )(FilmEvent.writes) shouldBe Json.parse(validJsonOfFilmDeleted)
      }

      it("""should not parse an unknown event type""") {
        Json.parse(invalidJsonOfFilmEvent).validate[FilmEvent] should matchPattern {
          case JsError(error :: otherErrors) =>
        }
      }

      it("""should not parse an event with malformed data""") {
        Json.parse(malformedDataJson).validate[FilmEvent] should matchPattern {
          case JsError(error :: otherErrors) =>
        }
      }
    }

  }

}
