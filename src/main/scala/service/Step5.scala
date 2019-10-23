package service

import play.api.libs.json._

object Step5 {

	//This is a JSON example of a Film
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
	val PickMetasFromFilm: Reads[JsValue] = (__ \ "additionalInfo" \ "metas").json.pick

	//TODO 5.2.1 create a reads that pick additionalInfo.metas value and validates it is a JsArray
	val PickMetasArrayFromFilm: Reads[JsArray] = (__ \ "additionalInfo" \ "metas").json.pick[JsArray]

	//TODO 5.3.1 create a reads that extract the additionalInfo.metas branch in the JSON of a film
	val PickBranchFromFilm: Reads[JsObject] = (__ \ "additionalInfo" \ "metas").json.pickBranch

	//TODO 5.4.1 create a reads that push a new value in the additionalInfo.metas array
	val createPushNewMetaInFilm: String  => Reads[JsObject] = newMeta => (__ \ "additionalInfo" \ "metas").json.update(
		__.read[JsArray].map(jsArray => jsArray :+ JsString(newMeta))
	)

	//TODO 5.5.1 create a reads that removes the additionalInfo.notes in tha JSON of a Film
	val PruneNotesFromFilm: Reads[JsObject] = (__ \ "additionalInfo" \ "notes").json.prune

	//TODO 5.1.2: create a function that uses PickMetasFromFilm to return the value of additionalInfo.metas value in the JSON of a Film
	def pickMetas(jsval: JsValue): JsResult[JsValue] = PickMetasFromFilm.reads(jsval)

	//TODO 5.2.2 create a function that uses PickMetasArrayFromFilm to return the value of additionalInfo.metas as a JsArray
	def pickMetasArray(jsval: JsValue): JsResult[JsArray] = PickMetasArrayFromFilm.reads(jsval)

	//TODO 5.3.2 create a function that uses PickBranchFromFilm to return a JSON of Film that contains only the additionalInfo.metas branch
	def pickMetasBranch(jsval: JsValue): JsResult[JsObject] = PickBranchFromFilm.reads(jsval)

	//TODO 5.4.2 create a function that uses createPushNewMetaInFilm to instanciate a reads and return the JSON of a Film with the specified meta added
	def pushNewMeta(jsObj: JsObject, newMeta: String): JsResult[JsObject] = createPushNewMetaInFilm(newMeta).reads(jsObj)

	//TODO 5.5.2 create a function that uses PruneNotesFromFilm to read the JSON of a Film and return it without additionalInfo.notes
	def pruneNotes(jsObj: JsObject): JsResult[JsObject] = PruneNotesFromFilm.reads(jsObj)

	//TODO 5.6 create a function that composes createPushNewMetaInFilm and PruneNotesFromFilm
	// to read the JSON of a Film and update it with a new meta and no more notes
	def pushNewMetaAndPruneNotes(jsObj: JsObject, newMeta: String): JsResult[JsObject] = {
		(createPushNewMetaInFilm(newMeta) andThen PruneNotesFromFilm).reads(jsObj)
	}

}
