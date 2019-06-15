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

	object fromJson {
		def unapply(message: JsValue): Option[FilmDeleted] = message.asOpt[FilmDeleted]
	}

}
