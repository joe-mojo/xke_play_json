package models

import play.api.libs.json._

import scala.util.Try


sealed trait FilmEvent {
  val data: Film
}

case class FilmCreated(data: Film) extends FilmEvent

object FilmCreated {

  implicit val reads: Reads[FilmCreated] = Reads { (value: JsValue) => value match {
    case parsed: JsObject =>
      Try {
        ((parsed \ "eventType").as[String], (parsed \ "dataType").as[String]) match {
          case ("created", "film") => JsSuccess(new FilmCreated((parsed \ "data").as[Film]))
        }
      }.getOrElse {
        JsError("Not a FilmCreated")
      }

    case default => JsError("Not a FilmCreated")
  }
  }

  object fromJson {

    def unapply(message: JsValue) = message.asOpt[FilmCreated]

  }

}

case class FilmUpdated(data: Film) extends FilmEvent

object FilmUpdated {

  implicit val reads: Reads[FilmUpdated] = Reads { (value: JsValue) => value match {
    case parsed: JsObject =>
      Try {
        ((parsed \ "eventType").as[String], (parsed \ "dataType").as[String]) match {
          case ("updated", "film") => JsSuccess(new FilmUpdated((parsed \ "data").as[Film]))
        }
      }.getOrElse {
        JsError("Not a FilmUpdated")
      }

    case default => JsError("Not a FilmUpdated")
  }
  }

  object fromJson {

    def unapply(message: JsValue) = message.asOpt[FilmUpdated]

  }

}

case class FilmDeleted(data: Film) extends FilmEvent

object FilmDeleted {

  implicit val reads: Reads[FilmDeleted] = Reads { (value: JsValue) => value match {
    case parsed: JsObject =>
      Try {
        ((parsed \ "eventType").as[String], (parsed \ "dataType").as[String]) match {
          case ("deleted", "film") => JsSuccess(new FilmDeleted((parsed \ "data").as[Film]))
        }
      }.getOrElse {
        JsError("Not a FilmDeleted")
      }

    case default => JsError("Not a FilmDeleted")
  }
  }

  object fromJson {

    def unapply(message: JsValue) = message.asOpt[FilmDeleted]

  }

}
