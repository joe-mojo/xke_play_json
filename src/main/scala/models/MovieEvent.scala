package models

import play.api.libs.json._

import scala.util.Try

/*
 TODO 2.7 Create Reads for MovieEvent and implementing classes
 A MovieEvent JSON looks like :
 {
  "eventType": <event type>,
  "dataType": "movie",
  "data": <movie json object>
 }
 <event type> can be "created", "updated" or "deleted"
 see models.MovieEventSpec for details.
 */
/*
 TODO 3.7 Create Writes for MovieEvent and implementing classes
 */
sealed trait MovieEvent {
  val data: Movie
}

case class MovieCreated(data: Movie) extends MovieEvent

object MovieCreated {
	/*
	 TODO 2.7.1 create Reads of MovieCreated
	  Remember, JsResult is a full fledged monad. Feel free to use a for comprehension
	 */
	implicit val readsWithoutEx: Reads[MovieCreated] = Reads {
		case parsed: JsObject =>
			for {
				evtType <- (parsed \ "eventType").validate[String] if evtType == "created"
				dataType <-  (parsed \ "dataType").validate[String] if dataType == "movie"
				movieData <- (parsed \ "data").validate[Movie]
			} yield MovieCreated(movieData)
		case otherJsValue => JsError(s"A JSON object is expected for MovieCreated: $otherJsValue")
	}

	/*
	 TODO 3.7.1 create Writes of MovieCreated
	  Remember
	  - some JSON attributes are not in the corresponding Scala object, you must write them literally
	  - one JSON attribute contains an object for which we already have a Writes (created in 3.5). We must use it.
	 */
	implicit val writes: Writes[MovieCreated] = Writes { evt =>
		Json.obj(
			"eventType" -> JsString("created"),
			"dataType" -> JsString("movie"),
			"data" -> Json.toJson(evt.data)
		)
	}

	object fromJson {
		def unapply(message: JsValue): Option[MovieCreated] = message.asOpt[MovieCreated]
	}

}

case class MovieUpdated(data: Movie) extends MovieEvent

object MovieUpdated {
	/*
	 TODO 2.7.2 create Reads of MovieUpdated
	 */
	implicit val reads: Reads[MovieUpdated] = Reads {
		case parsed: JsObject =>
			for {
				evtType <- (parsed \ "eventType").validate[String] if evtType == "updated"
				dataType <- (parsed \ "dataType").validate[String] if dataType == "movie"
				movieData <- (parsed \ "data").validate[Movie]
			} yield MovieUpdated(movieData)
		case otherJsValue => JsError(s"Not a MovieUpdated because not even a JsObject: $otherJsValue")
	}
	/*
	 TODO 3.7.2 create Writes of MovieUpdated
	  Same observations as 3.7.1
	 */
	implicit val writes: Writes[MovieUpdated] = Writes { evt =>
		Json.obj(
			"eventType" -> JsString("updated"),
			"dataType" -> JsString("movie"),
			"data" -> Json.toJson(evt.data)
		)
	}

  object fromJson {
    def unapply(message: JsValue): Option[MovieUpdated] = message.asOpt[MovieUpdated]
  }

}

case class MovieDeleted(data: Movie) extends MovieEvent

object MovieDeleted {
	/*
	 TODO 2.7.3 create Reads for MovieDeleted
	 */
	implicit val reads: Reads[MovieDeleted] = Reads {
		case parsed: JsObject =>
			for {
				evtType <- (parsed \ "eventType").validate[String] if evtType == "deleted"
				dataType <- (parsed \ "dataType").validate[String] if dataType == "movie"
				movieData <- (parsed \ "data").validate[Movie]
			} yield MovieDeleted(movieData)
		case otherJsValue => JsError(s"Not a MovieDeleted because not event a JsObject: $otherJsValue")
	}
	/*
	 TODO 3.7.3 create Writes of MovieDeleted
	  Same observations as 3.7.1
	 */
	implicit val writes: Writes[MovieDeleted] = Writes { evt =>
		Json.obj(
			"eventType" -> JsString("deleted"),
			"dataType" -> JsString("movie"),
			"data" -> Json.toJson(evt.data)
		)
	}

	object fromJson {
		def unapply(message: JsValue): Option[MovieDeleted] = message.asOpt[MovieDeleted]
	}

}

object MovieEvent {
	/*
	 TODO 2.7.4 create Reads for MovieEvent
	  This Reads should make you program compile when using as[MovieEvent] and validate[MovieEvent]:
	  it must first figure out which kind of instance it is reading, then just use specific formerly created Reads for MovieCreated, MovieUpdated and MovieDeleted.
	   > Once this one is done, are other specific Reads really useful ?
	   < Yes, this will allow this Reads to use them; but they sometime could be refactored
	 */
	implicit val reads: Reads[MovieEvent] = Reads {
		case jsObj: JsObject =>
			(jsObj \ "eventType").validate[String].flatMap {
				case "created" => jsObj.validate[MovieCreated]
				case "updated" => jsObj.validate[MovieUpdated]
				case "deleted" => jsObj.validate[MovieDeleted]
				case other => JsError(s"Unknown MovieEvent type: $other")
			}
		case other =>
			JsError(s"Cannot parse as MovieEvent something that is not an object: $other")
	}
	/*
	 TODO 3.7.4 create Writes for MovieEvent
	  This writes should make your program compile when writing an instance declared as MovieEvent.
	  It must first determine the actual MovieEvent subtype, then use an already defined Writes.
	   > What happens if you don't pass explicitly specific Writes to `Json.toJson` ?

	   > How to prevent stack overflow without passing explicit Writes ?


	   > How do sealed traits help us here ?

	 */
	implicit val writes: Writes[MovieEvent] = null




}
