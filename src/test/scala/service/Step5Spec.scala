package service

import org.scalatest.{Inside, Matchers, TryValues, WordSpec}
import play.api.libs.json._

import scala.reflect.ClassTag
import scala.util.Try

class Step5Spec extends WordSpec with Matchers with Inside with TryValues {
	val spaceballsJson: JsValue = Json.parse(Step5.spaceballsJsonString)

	val spaceballsJsonNoMetas: JsValue = Json.parse(
		"""{
		  | "startTimestamp": 1400000000,
		  | "name": "Spaceballs",
		  | "additionalInfo": {
		  |     "notes": "Mel Brooks and Rick Moranis in a delirium"
		  |   },
		  |  "author": "Mel Brooks",
		  |  "types": ["comedy", "sci-fi"]
		  |}""".stripMargin
	)

	val spaceballsJsonSimpleMetas: JsValue = Json.parse(
		"""{
		  | "startTimestamp": 1400000000,
		  | "name": "Spaceballs",
		  | "additionalInfo": {
		  |     "metas": "fun, parody",
		  |     "notes": "Mel Brooks and Rick Moranis in a delirium"
		  |   },
		  |  "author": "Mel Brooks",
		  |  "types": ["comedy", "sci-fi"]
		  |}""".stripMargin
	)

	val spaceballsJsonWithNewMeta: JsValue = Json.parse(
		"""{
		  | "startTimestamp": 1400000000,
		  | "name": "Spaceballs",
		  | "additionalInfo": {
		  |     "metas": ["fun", "parody", "comedy"],
		  |     "notes": "Mel Brooks and Rick Moranis in a delirium"
		  |   },
		  |  "author": "Mel Brooks",
		  |  "types": ["comedy", "sci-fi"]
		  |}""".stripMargin
	)

	val spaceballsJsonWithoutNotes: JsValue = Json.parse(
		"""{
		  | "startTimestamp": 1400000000,
		  | "name": "Spaceballs",
		  | "additionalInfo": {
		  |     "metas": ["fun", "parody"]
		  |   },
		  |  "author": "Mel Brooks",
		  |  "types": ["comedy", "sci-fi"]
		  |}""".stripMargin
	)

	val spaceballsJsonWithNewMetaButNoNotes: JsValue = Json.parse(
		"""{
		  | "startTimestamp": 1400000000,
		  | "name": "Spaceballs",
		  | "additionalInfo": {
		  |     "metas": ["fun", "parody", "comedy"]
		  |   },
		  |  "author": "Mel Brooks",
		  |  "types": ["comedy", "sci-fi"]
		  |}""".stripMargin
	)

	val metasPath: JsPath = __ \ "additionalInfo" \ "metas"
	val notesPath: JsPath = __ \ "additionalInfo" \ "notes"

	def checkJsError(tested: => JsResult[JsValue], path: JsPath): Unit = {
		inside(tested) {
			case JsError(errors) => errors.count(_._1 == path) should be >= 1
		}
	}

	def checkJsSuccess(tested: => JsResult[JsValue], path: JsPath): Unit = {
		tested should matchPattern {
			case JsSuccess(_: JsValue, `path`) =>
		}
	}

	def checkT[T <: JsValue : ClassTag](tested: => JsResult[T], path: JsPath): Unit = {
		tested should matchPattern {
			case JsSuccess(_: T, `path`) =>
		}
	}

	def checkValue[T <: JsValue](tested: => JsResult[T], path: JsPath, expectedValue: T): Unit = {
		tested should matchPattern {
			case JsSuccess(`expectedValue`, `path`) =>
		}
	}

	"PickMetas" when {
		""""additionalInfo\metas" path exists""" should {
			"""read "metas" in "additionalInfo" as any JsValue""" in {

				checkJsSuccess(Step5.pickMetas(spaceballsJson), metasPath)
				checkJsSuccess(Step5.pickMetas(spaceballsJsonSimpleMetas), metasPath)

			}
		}
		""""additionalInfo\metas" path doesn't exist""" should {
			"""not read "metas" and return a JsError""" in {
				checkJsError(Step5.pickMetas(spaceballsJsonNoMetas), metasPath)
			}
		}
	}

	"PickMetasArrayFromMovie" when {
		""""additionalInfo\metas" path exists""" should {
			"""read "metas" in "additionalInfo" as JsArray of strings""" in {
				checkValue(Step5.pickMetasArray(spaceballsJson), metasPath, JsArray(Seq(JsString("fun"), JsString("parody"))))
			}
		}
		""""additionalInfo\metas" path doesn't exist""" should {
			"""not read "metas" and return a JsError""" in {
				checkJsError(Step5.pickMetasArray(spaceballsJsonNoMetas), metasPath)
			}
		}
		""""additionalInfo\metas" path is a string""" should {
			"return a JsError" in {
				checkJsError(Step5.pickMetasArray(spaceballsJsonSimpleMetas), metasPath)
			}
		}
	}

	"PickBranchFromMovie" when {
		""""additionalInfo\metas" path exists""" should {
			"return a JsObject with the selected branch only" in {
				inside(Step5.pickMetasBranch(spaceballsJson)) {
					case JsSuccess(value: JsObject, `metasPath`) =>
						value shouldBe Json.obj(
							"additionalInfo" -> Json.obj(
								"metas" -> JsArray(Seq(JsString("fun"), JsString("parody")))
							)
						)
				}
			}
		}
		""""additionalInfo\metas" path doesn't exist""" should {
			"return a JsError" in {
				checkJsError(Step5.pickMetasBranch(spaceballsJsonNoMetas), metasPath)
			}
		}
		""""additionalInfo\metas" path is a string""" should {
			"return a JsObject with the selected branch only" in {
				inside(Step5.pickMetasBranch(spaceballsJsonSimpleMetas)) {
					case JsSuccess(value: JsObject, `metasPath`) =>
						value shouldBe Json.obj(
							"additionalInfo" -> Json.obj(
								"metas" -> JsString("fun, parody")
							)
						)
				}
			}
		}
	}

	"createPushNewMetaInMovie" when {
		""""additionalInfo\metas" path exists""" should {
			"read the JsObject and push a new meta" in {
				spaceballsJson shouldBe a[JsObject]
				spaceballsJsonWithNewMeta shouldBe a[JsObject]
				checkValue(Step5.pushNewMeta(spaceballsJson.as[JsObject], "comedy"), metasPath, spaceballsJsonWithNewMeta)
			}
		}
	}

	"PruneNotesFromMovie" when {
		""""additionalInfo\notes" path exists""" should {
			"read the JsObject and remove notes" in {
				spaceballsJson shouldBe a[JsObject]
				spaceballsJsonWithoutNotes shouldBe a[JsObject]
				checkValue(Step5.pruneNotes(spaceballsJson.as[JsObject]), notesPath, spaceballsJsonWithoutNotes)
			}
		}
	}

	"createPushNewMetaInMovie followed by PruneNotesFromMovie" when {
		""""additionalInfo\metas" and "additionalInfo\notes" paths exist""" should {
			"push a new meta and remove notes" in {
				spaceballsJson shouldBe a[JsObject]
				spaceballsJsonWithoutNotes shouldBe a[JsObject]
				spaceballsJsonWithNewMetaButNoNotes shouldBe a[JsObject]
				inside(Step5.pushNewMetaAndPruneNotes(spaceballsJson.as[JsObject], "comedy")) {
					case JsSuccess(obj: JsObject, _) => obj shouldBe spaceballsJsonWithNewMetaButNoNotes
				}
			}
		}
	}
}
