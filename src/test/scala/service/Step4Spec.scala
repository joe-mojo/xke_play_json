package service

import models.{Author, Movie, MovieType}
import org.scalatest.{Inside, Matchers, WordSpec}
import play.api.libs.json.{JsArray, JsNumber, JsObject, JsString, JsSuccess, Json, Writes}

class Step4Spec extends WordSpec with Matchers with Inside {
	"readsOnlyAdditionalInfo" should {
		"""allow reading a Movie from an actual instance and an "additionalInfo" JSON""" in {
			val updateJson = Json.obj("additionalInfo" -> Json.obj(
				"imdbScore" -> JsNumber(8.2D),
				"imdbId" -> JsString("tt0208092"),
				"tags" -> JsArray(Seq(JsString("Comedy"), JsString("Crime")))
			))
			val movieIdInRequestPath = "tt0208092"

			val movieFromDb = Step4.db(movieIdInRequestPath)

			inside(updateJson.validate[Movie](Step4.readsOnlyAdditionalInfo(movieFromDb))) {
				case JsSuccess(Movie(974271600000L, "Snatch", infos, Some(Author("Guy Ritchie")), Seq(MovieType.Comedy, MovieType.Crime)), _) =>
					infos shouldBe (updateJson \ "additionalInfo").as[JsObject]
			}

		}
		"""update only "additionalInfo" field""" when {
				"""request entity contains ll fields""" in {
					val updateInfos = Json.obj("tags" -> JsArray(Seq(JsString("Comedy"), JsString("Crime"))))
					val updateJson = Json.toJson(
						Movie(0L, "Pwnd!", updateInfos, Some(Author("Nobody lol")), Seq.empty)
					)
					val movieIdInRequestPath = "tt0208092"

					val movieFromDb = Step4.db(movieIdInRequestPath)

					inside(updateJson.validate[Movie](Step4.readsOnlyAdditionalInfo(movieFromDb))) {
						case JsSuccess(Movie(974271600000L, "Snatch", infos, Some(Author("Guy Ritchie")), Seq(MovieType.Comedy, MovieType.Crime)), _) =>
							infos shouldBe updateInfos
					}
			}
		}
	}
	"getMovieLightView" should {
		"""return a JSON obj with only name and author""" when {
			"called with writesWithIgnore" in {
				implicit val movieWrites: Writes[Movie] = Step4.writesWithIgnore

				inside(Step4.getMovieLightView("tt0208092")) {
					case Right(simpleJson) =>
						simpleJson shouldBe Json.obj(
							"name" -> JsString("Snatch"),
							"author" -> JsString("Guy Ritchie")
						)
				}

				inside(Step4.getMovieLightView("tt0094012")) {
					case Right(simpleJson) =>
						simpleJson shouldBe Json.obj(
							"name" -> JsString("Spaceballs"),
							"author" -> JsString("Mel Brooks")
						)
				}
			}
			"called with writesSimpleJsObj" in {
				implicit val movieWrites: Writes[Movie] = Step4.writesSimpleJsObj

				inside(Step4.getMovieLightView("tt0208092")) {
					case Right(simpleJson) =>
						simpleJson shouldBe Json.obj(
							"name" -> JsString("Snatch"),
							"author" -> JsString("Guy Ritchie")
						)
				}

				inside(Step4.getMovieLightView("tt0094012")) {
					case Right(simpleJson) =>
						simpleJson shouldBe Json.obj(
							"name" -> JsString("Spaceballs"),
							"author" -> JsString("Mel Brooks")
						)
				}
			}
		}
	}
}
