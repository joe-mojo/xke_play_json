package service

import models.{Author, Film}
import org.scalatest.{Inside, Matchers, WordSpec}
import play.api.libs.json.{JsArray, JsNumber, JsObject, JsString, JsSuccess, Json, Writes}

class Step4Spec extends WordSpec with Matchers with Inside {
	"readsOnlyAdditionalInfo" should {
		"""allow reading a Film from an actual instance and an "additionalInfo" JSON""" in {
			val updateJson = Json.obj("additionalInfo" -> Json.obj(
				"imdbScore" -> JsNumber(8.2D),
				"imdbId" -> JsString("tt0208092"),
				"tags" -> JsArray(Seq(JsString("Comedy"), JsString("Crime")))
			))
			val filmIdInRequestPath = "tt0208092"

			val filmFromDb = Step4.db(filmIdInRequestPath)

			inside(updateJson.validate[Film](Step4.readsOnlyAdditionalInfo(filmFromDb))) {
				case JsSuccess(Film(974271600000L, "Snatch", infos, Some(Author("Guy Ritchie"))), _) =>
					infos shouldBe (updateJson \ "additionalInfo").as[JsObject]
			}

		}
		"""update only "additionalInfo" field""" when {
				"""request entity contains ll fields""" in {
					val updateInfos = Json.obj("tags" -> JsArray(Seq(JsString("Comedy"), JsString("Crime"))))
					val updateJson = Json.toJson(
						Film(0L, "Pwnd!", updateInfos, Some(Author("Nobody lol")))
					)
					val filmIdInRequestPath = "tt0208092"

					val filmFromDb = Step4.db(filmIdInRequestPath)

					inside(updateJson.validate[Film](Step4.readsOnlyAdditionalInfo(filmFromDb))) {
						case JsSuccess(Film(974271600000L, "Snatch", infos, Some(Author("Guy Ritchie"))), _) =>
							infos shouldBe updateInfos
					}
			}
		}
	}
	"getFilmLightView" should {
		"""return a JSON obj with only name and author""" when {
			"called with writesWithIgnore" in {
				implicit val filmWrites: Writes[Film] = Step4.writesWithIgnore

				inside(Step4.getFilmLightView("tt0208092")) {
					case Right(simpleJson) =>
						simpleJson shouldBe Json.obj("name" -> JsString("Snatch"), "author" -> JsString("Guy Ritchie"))
				}

				inside(Step4.getFilmLightView("tt0094012")) {
					case Right(simpleJson) =>
						simpleJson shouldBe Json.obj("name" -> JsString("Spaceballs"), "author" -> JsString("Mel Brooks"))
				}
			}

		}
	}
}
