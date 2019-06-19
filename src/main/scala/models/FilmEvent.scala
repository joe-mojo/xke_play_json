package models

import play.api.libs.json._

import scala.util.Try

/*
 TODO 2.7 Create Reads for FilmEvent and implementing classes
 A FilmEvent JSON looks like :
 {
  "eventType": <event type>,
  "dataType": "film",
  "data": <film json object>
 }
 <event type> can be "created", "updated" or "deleted"
 see models.FilmEventSpec for details.
 */
/*
 TODO 3.7 Create Writes for FilmEvent and implementing classes
 */
sealed trait FilmEvent {
  val data: Film
}

case class FilmCreated(data: Film) extends FilmEvent

object FilmCreated {
	/*
	 TODO 2.7.1 create Reads of FilmCreated
	  Remember, JsResult is a full fledged monad. Feel free to use a for comprehension
	 */
	implicit val readsWithoutEx: Reads[FilmCreated] = Reads {
		case parsed: JsObject =>
			for {
				evtType <- (parsed \ "eventType").validate[String] if evtType == "created"
				dataType <-  (parsed \ "dataType").validate[String] if dataType == "film"
				filmData <- (parsed \ "data").validate[Film]
			} yield FilmCreated(filmData)
		case otherJsValue => JsError(s"A JSON object is expected for FilmCreated: $otherJsValue")
	}

	/*
	 TODO 3.7.1 create Writes of FilmCreated
	  Remember
	  - some JSON attributes are not in the corresponding Scala object, you must write them literally
	  - one JSON attribute contains an object for which we already have a Writes (created in 3.5). We must use it.
	 */
	implicit val writes: Writes[FilmCreated] = Writes { evt =>
		Json.obj(
			"eventType" -> JsString("created"),
			"dataType" -> JsString("film"),
			"data" -> Json.toJson(evt.data)
		)
	}

	object fromJson {
		def unapply(message: JsValue): Option[FilmCreated] = message.asOpt[FilmCreated]
	}

}

case class FilmUpdated(data: Film) extends FilmEvent

object FilmUpdated {
	/*
	 TODO 2.7.2 create Reads of FilmUpdated
	 */
	implicit val reads: Reads[FilmUpdated] = Reads {
		case parsed: JsObject =>
			for {
				evtType <- (parsed \ "eventType").validate[String] if evtType == "updated"
				dataType <- (parsed \ "dataType").validate[String] if dataType == "film"
				filmData <- (parsed \ "data").validate[Film]
			} yield FilmUpdated(filmData)
		case otherJsValue => JsError(s"Not a FilmUpdated because not even a JsObject: $otherJsValue")
	}
	/*
	 TODO 3.7.2 create Writes of FilmUpdated
	  Same observations as 3.7.1
	 */
	implicit val writes: Writes[FilmUpdated] = Writes { evt =>
		Json.obj(
			"eventType" -> JsString("updated"),
			"dataType" -> JsString("film"),
			"data" -> Json.toJson(evt.data)
		)
	}

  object fromJson {
    def unapply(message: JsValue): Option[FilmUpdated] = message.asOpt[FilmUpdated]
  }

}

case class FilmDeleted(data: Film) extends FilmEvent

object FilmDeleted {
	/*
	 TODO 2.7.3 create Reads for FilmDeleted
	 */
	implicit val reads: Reads[FilmDeleted] = Reads {
		case parsed: JsObject =>
			for {
				evtType <- (parsed \ "eventType").validate[String] if evtType == "deleted"
				dataType <- (parsed \ "dataType").validate[String] if dataType == "film"
				filmData <- (parsed \ "data").validate[Film]
			} yield FilmDeleted(filmData)
		case otherJsValue => JsError(s"Not a FilmDeleted because not event a JsObject: $otherJsValue")
	}
	/*
	 TODO 3.7.3 create Writes of FilmDeleted
	  Same observations as 3.7.1
	 */
	implicit val writes: Writes[FilmDeleted] = Writes { evt =>
		Json.obj(
			"eventType" -> JsString("deleted"),
			"dataType" -> JsString("film"),
			"data" -> Json.toJson(evt.data)
		)
	}

	object fromJson {
		def unapply(message: JsValue): Option[FilmDeleted] = message.asOpt[FilmDeleted]
	}

}

object FilmEvent {
	/*
	 TODO 2.7.4 create Reads for FilmEvent
	  This Reads should make you program compile when using as[FilmEvent] and validate[FilmEvent]:
	  it must first figure out which kind of instance it is reading, then just use specific formerly created Reads for FilmCreated, FilmUpdated and FilmDeleted.
	   > Once this one is done, are other specific Reads really useful ?
	   < Yes, this will allow this Reads to use them; but they sometime could be refactored
	 */
	implicit val reads: Reads[FilmEvent] = Reads {
		case jsObj: JsObject =>
			(jsObj \ "eventType").validate[String].flatMap {
				case "created" => jsObj.validate[FilmCreated]
				case "updated" => jsObj.validate[FilmUpdated]
				case "deleted" => jsObj.validate[FilmDeleted]
				case other => JsError(s"Unknon FilmEvent type: $other")
			}
		case other =>
			JsError(s"Cannot parse as FilmEvent something that is not an object: $other")
	}
	/*
	 TODO 3.7.4 create Writes for FilmEvent
	  This writes should make your program compile when writing an instance declared as FilmEvent.
	  It must first determine the actual FilmEvent subtype, then use an already defined Writes.
	   > What happens if you don't pass explicitly specific Writes to `Json.toJson` ?
	   < StackOverflowError because Writes[FilmEvent] is calling itself because it is a suitable Writes for each subtype !
	   > How to prevent stack overflow without passing explicit Writes ?
	   < Isolate implicits Writes&Reads in a specific object in a specific package, like "com.company.appname.json.http.Implicits" or "com.company.appname.json.db.Implicits"
	     and import only needed implicits in each scope using Json.*
	   > How do sealed traits help us here ?
	   < compiler can tell if we forgot a FilmEvent subtype
	 */
	implicit val writes: Writes[FilmEvent] = Writes {
		case evt: FilmCreated => Json.toJson(evt)(FilmCreated.writes)
		case evt: FilmUpdated => Json.toJson(evt)(FilmUpdated.writes)
		case evt: FilmDeleted => Json.toJson(evt)(FilmDeleted.writes)
	}
}
