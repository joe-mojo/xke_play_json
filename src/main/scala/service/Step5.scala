package service

import play.api.libs.json._

object Step5 {

	//This is a JSON example of a Movie
	val spaceballsJsonString: String =
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

	//TODO 5.1.1 create a reads that pick additionalInfo.metas value
	val PickMetasFromMovie: Reads[JsValue] = (__ \ "additionalInfo" \ "metas").json.pick

	//TODO 5.2.1 create a reads that pick additionalInfo.metas value and validates it is a JsArray
	val PickMetasArrayFromMovie: Reads[JsArray] = null

	//TODO 5.3.1 create a reads that extract the additionalInfo.metas branch in the JSON of a movie
	val PickBranchFromMovie: Reads[JsObject] = null

	//TODO 5.4.1 create a reads that push a new value in the additionalInfo.metas array
	val createPushNewMetaInMovie: String  => Reads[JsObject] = { newMeta =>


		???
	}

	//TODO 5.5.1 create a reads that removes the additionalInfo.notes in tha JSON of a Movie
	val PruneNotesFromMovie: Reads[JsObject] = null

	//TODO 5.1.2: create a function that uses PickMetasFromMovie to return the value of additionalInfo.metas value in the JSON of a Movie
	def pickMetas(jsval: JsValue): JsResult[JsValue] = PickMetasFromMovie.reads(jsval)

	//TODO 5.2.2 create a function that uses PickMetasArrayFromMovie to return the value of additionalInfo.metas as a JsArray
	def pickMetasArray(jsval: JsValue): JsResult[JsArray] = ???

	//TODO 5.3.2 create a function that uses PickBranchFromMovie to return a JSON of Movie that contains only the additionalInfo.metas branch
	def pickMetasBranch(jsval: JsValue): JsResult[JsObject] = ???

	//TODO 5.4.2 create a function that uses createPushNewMetaInMovie to instanciate a reads and return the JSON of a Movie with the specified meta added
	def pushNewMeta(jsObj: JsObject, newMeta: String): JsResult[JsObject] = ???

	//TODO 5.5.2 create a function that uses PruneNotesFromMovie to read the JSON of a Movie and return it without additionalInfo.notes
	def pruneNotes(jsObj: JsObject): JsResult[JsObject] = ???

	//TODO 5.6 create a function that composes createPushNewMetaInMovie and PruneNotesFromMovie
	// to read the JSON of a Movie and update it with a new meta and no more notes
	def pushNewMetaAndPruneNotes(jsObj: JsObject, newMeta: String): JsResult[JsObject] = {
		???
	}

}
