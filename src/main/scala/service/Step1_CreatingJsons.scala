package service

import play.api.libs.json.{JsNumber, JsObject, JsString, JsValue, Json}

import scala.util.Try

object Step1_CreatingJsons {

	//TODO 1.1 Create a JSON parsing a String
	private final val snatch =
		"""
		  |{
		  |  "timestamp": 974271600000,
		  |  "name": "Snatch",
		  |  "additionalInfo": {
		  |    "imdbScore": 8.3
		  |  },
		  |  "author": "Guy Ritchie"
		  |}
		""".stripMargin

	def parseSnatch: JsValue = {
		Json.parse(snatch)
	}

	/*
	  TODO 1.2 Use Json.* to create a object representing this JSON :
	   {
	     "type": "Point",
	     "coordinates": [2.3088449, 48.8753487]
	   }
	 */
	def createYourJson: JsObject = {
		Json.obj(
			"type" -> JsString("Point"),
			"coordinates" -> Json.arr(BigDecimal(2.3088449), BigDecimal(48.8753487))
		)
	}

	/*
	  TODO 1.3 Use JsPath to check if the given Film, passed as JSON object, has a name being a string and an imdbScore being a number from 0 to 10 (inclusive bounds)
	  Return a (Boolean, Boolean) to tell if (name, idmbScore) are corrects. Remember, imdbScore is under "additionalInfo" sub-document.
	  hint: validate
	 */
	def checkThisJsonFilm(jsFilm: JsObject): (Boolean, Boolean) = {
		(
				(jsFilm \ "name").validate[String].fold(_ => false, _ => true),
				(jsFilm \ "additionalInfo" \ "imdbScore").validate[BigDecimal].fold(_ => false, n =>  n >= 0 && n<= 10),
		)
	}

	/*
	  TODO 1.4 Use JsPath to check imdbScore again, but this time we add one condition: valid "string" scores (like "5.2" with double quotes)
	  must NOT be coerced to Number and return false
	 */
	def checkImdbScore(jsFilm: JsObject): Boolean = {
		(jsFilm \ "additionalInfo" \ "imdbScore").validate[JsNumber].fold(_ => false, n => n.value >= 0 && n.value <= 10)
	}

	//TODO 1.5 See inside JsSuccess and JSError

	//TODO 1.6 validate using JsSuccess and JsError instead of Boolean

}
