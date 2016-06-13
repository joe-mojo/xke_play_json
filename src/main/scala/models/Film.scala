package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Film(
                 timestamp: Long,
                 name: String,
                 additionalInfo: JsValue,
                 author: Option[Author]
               )

object Film {

  implicit val reads: Reads[Film] = {
    (
      (__ \ "startTimestamp").read[Long] and
        (__ \ "name").read[String] and
        (__ \ "additionalInfo").read[JsValue] and
        (__ \ "author").readNullable[Author]
      ) (Film.apply _)
  }

  implicit val writes: Writes[Film] = {
    (
      (__ \ "startTimestamp").write[Long] and
        (__ \ "name").write[String] and
        (__ \ "additionalInfo").write[JsValue] and
        (__ \ "author").writeNullable[Author]
      ) (unlift(Film.unapply))
  }
}

case class Author(name: String)

object Author {
  implicit val reads: Reads[Author] = {
    __.read[String].map(Author(_))
  }

  implicit val writes: Writes[Author] = {
    __.write[String].contramap(_.name)
  }
}
