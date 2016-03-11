package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Film(
                 timestamp: String,
                 name: String,
                 additionalInfo: JsValue
               )

object Film {

  implicit val reads: Reads[Film] = {
    (
      (__ \ "startTimestamp").read[String] and
        (__ \ "name").read[String] and
        (__ \ "additionalInfo").read[JsValue]
      ) (Film.apply _)
  }

  implicit val writes: Writes[Film] = {
    (
      (__ \ "startTimestamp").write[String] and
        (__ \ "name").write[String] and
        (__ \ "additionalInfo").write[JsValue]
      ) (unlift(Film.unapply))
  }
}
