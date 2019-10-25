package models

import org.scalatest.{FunSpec, Inside, Matchers}
import play.api.libs.json._

class MovieEventSpec extends FunSpec with Matchers with Inside{
  private final val validJsonOfMovieCreated =
    """
      | {
      |   "eventType": "created",
      |   "dataType": "movie",
      |   "data": {
      |     "startTimestamp": 1400000000,
      |     "name": "Spaceballs",
      |     "additionalInfo": {
      |       "metas": ["fun", "parody"],
      |       "notes": "Mel Brooks and Rick Moranis in a delirium"
      |     },
      |     "author": "Mel Brooks",
	  |     "types": ["comedy", "sci-fi"]
      |   }
      | }
    """.stripMargin
  private final val validJsonOfMovieUpdated =
          """
            | {
            |   "eventType": "updated",
            |   "dataType": "movie",
            |   "data": {
            |     "startTimestamp": 1400000000,
            |     "name": "Spaceballs",
            |     "additionalInfo": {
            |       "metas": ["fun", "parody"],
            |       "notes": "Mel Brooks and Rick Moranis in a delirium"
            |     },
            |     "author": "Mel Brooks",
            |     "types": ["comedy", "sci-fi"]
            |   }
            | }
          """.stripMargin
  private final val validJsonOfMovieDeleted =
    """
      | {
      |   "eventType": "deleted",
      |   "dataType": "movie",
      |   "data": {
      |     "startTimestamp": 1400000000,
      |     "name": "Spaceballs",
      |     "author": "Mel Brooks",
      |     "additionalInfo": null,
      |     "types": ["comedy", "sci-fi"]
      |   }
      | }
    """.stripMargin
  private final val invalidJsonOfMovieCreated =
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
      |     "author": "Mel Brooks",
      |     "types": ["comedy", "sci-fi"]
      |   }
      | }
    """.stripMargin
  private final val invalidJsonOfMovieEvent =
    """
      | {
      |   "eventType": "schtroumpfed",
      |   "dataType": "movie",
      |   "data": {
      |     "startTimestamp": 1400000000,
      |     "name": "Spaceballs",
      |     "additionalInfo": {
      |       "metas": ["fun", "parody"],
      |       "notes": "Mel Brooks and Rick Moranis in a delirium"
      |     },
      |     "author": "Mel Brooks",
      |     "types": ["comedy", "sci-fi"]
      |   }
      | }
    """.stripMargin
  private final val malformedDataJson = """
    | {
    |   "eventType": "created",
    |   "dataType": "movie",
    |   "data": {
    |     "startTimestamp": 1400000000,
    |     "name": "Spaceballs",
    |     "author": "Mel Brooks",
    |     "types": ["comedy", "sci-fi"]
    |   }
    | }
  """.stripMargin

  val spaceballTypes = Seq(MovieType.Comedy, MovieType.SciFi)

  describe("A MovieCreated event") {
    it("""should parse a JSON movie event with "created" type and "movie" data type""") {
      inside(Json.parse(validJsonOfMovieCreated).validate[MovieCreated])  {
        case JsSuccess(movieEvt, path) =>
          movieEvt should be(MovieCreated(Movie(1400000000L, "Spaceballs", Json.obj(
            "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
            "notes" -> "Mel Brooks and Rick Moranis in a delirium"
          ), Some(Author("Mel Brooks")), spaceballTypes)))
      }
    }

    it("""should render a MovieCreated as a valid JSON""") {
      Json.toJson(MovieCreated(Movie(1400000000L, "Spaceballs", Json.obj(
        "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
        "notes" -> "Mel Brooks and Rick Moranis in a delirium"
      ), Some(Author("Mel Brooks")), spaceballTypes)))(MovieCreated.writes) shouldBe Json.parse(validJsonOfMovieCreated)
    }

    it("""should render a MovieUpdated as a valid JSON""") {
      Json.toJson(MovieUpdated(Movie(1400000000L, "Spaceballs", Json.obj(
        "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
        "notes" -> "Mel Brooks and Rick Moranis in a delirium"
      ), Some(Author("Mel Brooks")), spaceballTypes)))(MovieUpdated.writes) shouldBe Json.parse(validJsonOfMovieUpdated)
    }

    it("""should render a MovieDeleted as a valid JSON""") {
      Json.toJson(
        MovieDeleted(Movie(1400000000L, "Spaceballs", JsNull, Some(Author("Mel Brooks")), spaceballTypes))
      )(MovieDeleted.writes) shouldBe Json.parse(validJsonOfMovieDeleted)
    }

    it("""should not parse a JSON movie event with a type other than "created"""") {
      Json.parse(validJsonOfMovieUpdated).validate[MovieCreated] should matchPattern {
        case JsError(errors) =>
      }
    }

    it("""should not parse a JSON movie event with "created" type but wrong dataType""") {
      Json.parse(invalidJsonOfMovieCreated).validate[MovieCreated] should matchPattern {
        case JsError(errors) =>
      }
    }
  }

  describe("A MovieEvent") {
    describe("using only MovieEvent trait") {
      it("""should parse a "created" JSON movie event""") {
        inside(Json.parse(validJsonOfMovieCreated).validate[MovieEvent]) {
          case JsSuccess(MovieCreated(Movie(1400000000L, "Spaceballs", infos: JsObject, Some(Author("Mel Brooks")), `spaceballTypes`)), _) =>
            inside((infos \ "metas").validate[Array[String]]) {
              case JsSuccess(Array("fun", "parody"), _) =>
            }
            (infos \ "notes").asOpt[String] should contain("Mel Brooks and Rick Moranis in a delirium")
            infos.keys should contain theSameElementsAs Seq("metas", "notes")
        }
      }
      it("""should render a "created" JSON movie event""") {
        Json.toJson(MovieCreated(Movie(1400000000L, "Spaceballs", Json.obj(
          "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
          "notes" -> "Mel Brooks and Rick Moranis in a delirium"
        ), Some(Author("Mel Brooks")), spaceballTypes)))(MovieEvent.writes) shouldBe Json.parse(validJsonOfMovieCreated)
      }

      it("""should parse an "updated" JSON movie event""") {
        inside(Json.parse(validJsonOfMovieUpdated).validate[MovieEvent]) {
          case JsSuccess(MovieUpdated(Movie(1400000000L, "Spaceballs", infos: JsObject, Some(Author("Mel Brooks")), `spaceballTypes`)), _) =>
            inside((infos \ "metas").validate[Array[String]]) {
              case JsSuccess(Array("fun", "parody"), _) =>
            }
            (infos \ "notes").asOpt[String] should contain("Mel Brooks and Rick Moranis in a delirium")
            infos.keys should contain theSameElementsAs Seq("metas", "notes")
        }
      }
      it("""should render an "updated" JSON movie event""") {
        Json.toJson(MovieUpdated(Movie(1400000000L, "Spaceballs", Json.obj(
          "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
          "notes" -> "Mel Brooks and Rick Moranis in a delirium"
        ), Some(Author("Mel Brooks")), spaceballTypes)))(MovieEvent.writes) shouldBe Json.parse(validJsonOfMovieUpdated)
      }

      it("""should parse a "deleted" JSON movie event""") {
        Json.parse(validJsonOfMovieDeleted).validate[MovieEvent] should matchPattern {
          case JsSuccess(MovieDeleted(Movie(1400000000L, "Spaceballs", JsNull, Some(Author("Mel Brooks")), `spaceballTypes`)), _) =>
        }
      }
      it("""should render a "deleted" JSON movie event""") {
        Json.toJson(
          MovieDeleted(Movie(1400000000L, "Spaceballs", JsNull, Some(Author("Mel Brooks")), spaceballTypes))
        )(MovieEvent.writes) shouldBe Json.parse(validJsonOfMovieDeleted)
      }

      it("""should not parse an unknown event type""") {
        Json.parse(invalidJsonOfMovieEvent).validate[MovieEvent] should matchPattern {
          case JsError(error :: otherErrors) =>
        }
      }

      it("""should not parse an event with malformed data""") {
        Json.parse(malformedDataJson).validate[MovieEvent] should matchPattern {
          case JsError(error :: otherErrors) =>
        }
      }
    }

  }

}
