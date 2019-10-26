package models

import org.scalatest.{FunSpec, Inside, Matchers}
import play.api.libs.json._

class MovieSpec extends FunSpec with Matchers with Inside {
  val spaceballTypes: Seq[MovieType] = Seq(MovieType.Comedy, MovieType.SciFi)

  val fullJson: String = """{
                   | "startTimestamp": 1400000000,
                   | "name": "Spaceballs",
                   | "additionalInfo": {
                   |     "metas": ["fun", "parody"],
                   |     "notes": "Mel Brooks and Rick Moranis in a delirium"
                   |   },
                   |  "author": "Mel Brooks",
                   |  "types": ["comedy", "sci-fi"]
                   |}""".stripMargin

  val simpleJson: String = """{
                       | "startTimestamp": 1400000000,
                       | "name": "Spaceballs",
                       | "additionalInfo": {}
                       |}""".stripMargin

  val jsonWithStringInfo: String =
    """{
      | "startTimestamp": 1400000000,
      | "name": "Spaceballs",
      | "additionalInfo": "Nice",
      | "author": "Mel Brooks"
      |}""".stripMargin

  val jsonWithArrayInfo: String =
    """{
      | "startTimestamp": 1400000000,
      | "name": "Spaceballs",
      | "additionalInfo": [42, 1337, "Bim !"],
      | "author": "Mel Brooks"
      |}""".stripMargin

  val jsonWithNullInfo: String =
    """{
      | "startTimestamp": 1400000000,
      | "name": "Spaceballs",
      | "additionalInfo": null,
      | "author": "Mel Brooks"
      |}""".stripMargin

  describe("A Movie") {
    it("should read a simple JSON") {
      val jsonInput = fullJson

      val validatedInput = Json.parse(jsonInput).validate[Movie]
      inside(validatedInput) {
        case JsSuccess(value, _) => value should be(Movie(1400000000L, "Spaceballs", Json.obj(
          "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
          "notes" -> "Mel Brooks and Rick Moranis in a delirium"
        ), Some(Author("Mel Brooks")), spaceballTypes))
      }
    }

    it("should read a JSON without Author, empty object as additional infos and an empty type list") {
      val jsonInput = simpleJson

      inside(Json.parse(jsonInput).validate[Movie]) {
        case JsSuccess(value, _) => value should be(Movie(1400000000L, "Spaceballs", Json.obj(), None, Seq.empty))
      }
      /* Just for example: instead of "validate" (that is returning an Either-like functor), we can use "as" (that is returning the right type but
       raising exception in case of error)
     */
      Json.parse(jsonInput).as[Movie] should be(Movie(1400000000L, "Spaceballs", Json.obj(), None, Seq.empty))
    }

    it("should read a JSON with additionalInfo, when the type of JSON data is other than object") {

      val validatedInputs = Seq(jsonWithArrayInfo, jsonWithNullInfo, jsonWithStringInfo)
              .map(Json.parse).map(_.validate[Movie])

      validatedInputs should matchPattern {
        case JsSuccess(v1, p1) :: JsSuccess(v2, p2) :: JsSuccess(v3, p3) :: Nil =>
      }

      val movies = validatedInputs.map(_.get)
      movies.foreach { movie =>
        movie.timestamp should be(1400000000)
        movie.name should be("Spaceballs")
        movie.author should be(Some(Author("Mel Brooks")))
      }

      movies.head.additionalInfo should matchPattern {
        case JsArray(Seq(JsNumber(n), JsNumber(m), JsString("Bim !"))) if n == BigDecimal(42) && m == BigDecimal(1337) =>
      }

      movies(1).additionalInfo should matchPattern {
        case JsNull =>
      }


      movies.last.additionalInfo should matchPattern {
        case JsString("Nice") =>
      }

    }

    it("should write its JSON when author is defined") {
      val givenMovie = Movie(1400000000L, "Spaceballs", Json.obj(
        "metas" -> JsArray(Seq(JsString("fun"), JsString("parody"))),
        "notes" -> "Mel Brooks and Rick Moranis in a delirium"
      ), Option(Author("Mel Brooks")), spaceballTypes)

      Json.toJson(givenMovie) shouldBe Json.parse(fullJson)
    }

    it("should write its JSON when author, additionalInfo and types are empty"){
      val givenMovie = Movie(1400000000L, "Spaceballs", Json.obj(), None, Seq.empty)

      Json.toJson(givenMovie) shouldBe (Json.parse(simpleJson).as[JsObject] + ("types", JsArray(Seq.empty)) )
    }

    it("should write its JSON when the type of JSON data is other than object") {
      val givenMovieWithStringInfo = Movie(1400000000L, "Spaceballs", JsString("Nice"), Option(Author("Mel Brooks")), Seq.empty)
      val givenMovieWithArrayInfo = Movie(1400000000L, "Spaceballs", JsArray(Seq(JsNumber(42), JsNumber(1337), JsString("Bim !"))), Option(Author("Mel Brooks")), Seq.empty)
      val givenMovieWithNullInfo = Movie(1400000000L, "Spaceballs", JsNull, Option(Author("Mel Brooks")), Seq.empty)

      Json.toJson(givenMovieWithStringInfo) shouldBe Json.parse(jsonWithStringInfo).as[JsObject] + ("types", JsArray(Seq.empty))
      Json.toJson(givenMovieWithArrayInfo) shouldBe Json.parse(jsonWithArrayInfo).as[JsObject] + ("types", JsArray(Seq.empty))
      Json.toJson(givenMovieWithNullInfo) shouldBe Json.parse(jsonWithNullInfo).as[JsObject] + ("types", JsArray(Seq.empty))
    }

  }

  describe("A movie author") {
    it("should read Author from a string") {
      val inputJson = Json.obj("author" -> JsString("Todd Phillips"))

      (inputJson \ "author").validate[Author] should matchPattern {
        case JsSuccess(Author("Todd Phillips"), _) =>
      }

      Json.parse(""""Mel Brooks"""").validate[Author] should matchPattern {
        case JsSuccess(Author("Mel Brooks"), _) =>
      }

    }

    it("should write its JSON as a string") {
      Json.toJson(Author("Todd Phillips")) shouldBe JsString("Todd Phillips")
    }

  }
}

