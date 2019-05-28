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
  /*
    TODO 2.5 Create a custom Reads for Film. In expected JSON, the timestamp attribute is named "startTimestamp"
    Examples:
    {"startTimestamp": 974271600000, "name": "Snatch", "additionalInfo" : {"whatever": "you want"}, "author": "Guy Richie"}
    {"startTimestamp": 974271600000, "name": "Snatch", "additionalInfo" : "Whatever you want"}
   */
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
  /*
  TODO 2.4 Create a Reads for Author. Inside Json, an Author is only a String:  {..., "author": "Lana Wachowsky", ...}
  There is 2 ways of doing this.
   */
  val readsByPath: Reads[Author] = {
    __.read[String].map(Author(_))
  }
  val readsByStringReader: Reads[Author] = Reads.of[String].map(Author.apply)
  implicit val reads: Reads[Author] = readsByStringReader

  implicit val writes: Writes[Author] = {
    __.write[String].contramap(_.name)
  }
}
